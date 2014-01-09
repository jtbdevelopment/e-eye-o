package com.jtbdevelopment.e_eye_o.DAO

import com.jtbdevelopment.e_eye_o.entities.*
import org.joda.time.LocalDate
import org.joda.time.LocalDateTime
import org.joda.time.LocalTime
import org.testng.annotations.Test

/**
 * Date: 12/31/13
 * Time: 11:49 PM
 */
abstract class AbstractReadIntegration extends AbstractIntegration {
    public static AppUser user
    public static List<AppUserOwnedObject> objects = []

    @Test(groups = ["integration"])
    public void testGetStudentsForClassList() {
        initData()
        Map<ClassList, List<Student>> studentByClass = createEmptyMap(ClassList, { [(it): (List<Student>) []] })
        objects.findAll { it in Student }.each {
            Student student ->
                student.classLists.each { studentByClass[it].add(student) }
        }
        studentByClass.each {
            ClassList classList, List<Student> students ->
                deepCompare(rwDAO.getAllStudentsForClassList(classList), students as Set)
        }
    }

    @Test(groups = ["integration"])
    public void testGetObservationsForSemester() {
        initData()
        Map<Semester, List<Observation>> obsBySemester = createEmptyMap(Semester, { [(it): []] })
        objects.findAll { it in Observation }.each {
            Observation obs ->
                LocalDate date = obs.observationTimestamp.toLocalDate()
                obsBySemester.keySet().each {
                    if (date.compareTo(it.start) >= 0 && date.compareTo(it.end) <= 0) {
                        obsBySemester.get(it).add(obs)
                    }
                }
        }
        obsBySemester.each {
            Semester semester, List<Observation> obs ->
                deepCompare(rwDAO.getAllObservationsForSemester(semester).toList(), obs as Set)
        }
    }

    @Test(groups = ["integration"])
    public void testGetObservationsForCategory() {
        initData()
        Map<ObservationCategory, List<Observation>> obsByCategory = createEmptyMap(ObservationCategory, { [(it): []] })
        obsByCategory.put(null, [])
        objects.findAll { it in Observation }.each {
            Observation obs ->
                obs.categories.each { obsByCategory[it].add(obs) }
                if (obs.categories.isEmpty()) {
                    obsByCategory[null].add(obs)
                }
        }
        obsByCategory.each {
            ObservationCategory cat, List<Observation> obs ->
                deepCompare(rwDAO.getAllObservationsForObservationCategory(user, cat), obs as Set)
        }
    }

    @Test(groups = ["integration"])
    public void testGetObservationsForEntity() {
        initData()
        Map<Observable, List<Observation>> obsBySubject = createEmptyMap(Observable, { [(it): []] })
        objects.findAll { it in Observation }.each {
            Observation obs ->
                obsBySubject[obs.observationSubject].add(obs)
        }
        obsBySubject.each {
            Observable subject, List<Observation> obs ->
                deepCompare(rwDAO.getAllObservationsForEntity(subject), obs as Set)
        }
    }

    @Test(groups = ["integration"])
    public void testGetObservationsForEntityAndCategory() {
        initData()
        Map<ObservationCategory, Map<Observable, List<Observation>>> obsByCatAndEntity = createEmptyMap(ObservationCategory, {
            [(it): [:]]
        })
        obsByCatAndEntity.put(null, [:])
        obsByCatAndEntity.each {
            ObservationCategory cat, Map<Observable, List<Observation>> observable ->
                objects.findAll { (it in Observable) }.each {
                    Observable it ->
                        observable.put(it, [])
                }
        }
        objects.findAll { it in Observation }.each {
            Observation obs ->
                obs.categories.each { obsByCatAndEntity[it][obs.observationSubject].add(obs) }
                if (obs.categories.isEmpty()) {
                    obsByCatAndEntity[null][obs.observationSubject].add(obs)
                }
        }
        obsByCatAndEntity.each {
            ObservationCategory cat, Map<Observable, List<Observation>> observables ->
                observables.each {
                    Observable observable, List<Observation> obs ->
                        deepCompare(rwDAO.getAllObservationsForEntityAndCategory(observable, cat, LocalDate.now().minusYears(100), LocalDate.now().plusYears(100)), obs as Set)
                }
        }
    }

    @Test(groups = ["integration"])
    public void testGetPhotosForEntity() {
        initData()
        Map<AppUserOwnedObject, List<Photo>> photosBySubject = createEmptyMap(AppUserOwnedObject, { [(it): []] })
        objects.findAll { it in Photo }.each {
            Photo photo ->
                photosBySubject[photo.photoFor].add(photo)
        }
        photosBySubject.each {
            AppUserOwnedObject subject, List<Photo> photos ->
                List active = photos.findAll { !it.archived }
                List archived = photos.findAll { it.archived }

                assert photos.size() == rwDAO.getAllPhotosForEntityCount(subject)
                deepCompare(rwDAO.getAllPhotosForEntity(subject, 0, 0), photos as Set)
                assert active.size() == rwDAO.getActivePhotosForEntityCount(subject)
                deepCompare(rwDAO.getActivePhotosForEntity(subject, 0, 0), active as Set)
                assert archived.size() == rwDAO.getArchivedPhotosForEntityCount(subject)
                deepCompare(rwDAO.getArchivedPhotosForEntity(subject, 0, 0), archived as Set)
        }
    }

    @Test(groups = ["integration"])
    public void testGetPhotosForEntityPaginated() {
        initData()
        Map<AppUserOwnedObject, List<Photo>> photosBySubject = createEmptyMap(AppUserOwnedObject, { [(it): []] })
        objects.findAll { it in Photo }.each {
            Photo photo ->
                photosBySubject[photo.photoFor].add(photo)
        }
        photosBySubject.each {
            AppUserOwnedObject subject, List<Photo> photos ->
                List active = photos.findAll { !it.archived }
                List archived = photos.findAll { it.archived }

                List actuals

                actuals = retrievePaginatedData(photos.size(), { int page, int size -> rwDAO.getAllPhotosForEntity(subject, page * size, size) })
                assert photos.size() == actuals.size()
                deepCompare(actuals, photos.toSet())

                actuals = retrievePaginatedData(active.size(), { int page, int size -> rwDAO.getActivePhotosForEntity(subject, page * size, size) })
                assert active.size() == actuals.size()
                deepCompare(actuals, active.toSet())

                actuals = retrievePaginatedData(archived.size(), { int page, int size -> rwDAO.getArchivedPhotosForEntity(subject, page * size, size) })
                assert archived.size() == actuals.size()
                deepCompare(actuals, archived.toSet())
        }
    }

    @Test(groups = ["integration"])
    public void testReadEntitiesByType() {
        initData()
        List<Class<? extends AppUserOwnedObject>> classes = [AppUserOwnedObject, Semester, Observation, Semester, Photo, Student, Observable, ClassList, AppUserSettings, TwoPhaseActivity]
        //  TODO optional tests on deleted objects
        for (Class<? extends AppUserOwnedObject> c : classes) {
            println "Checking " + c.canonicalName

            List<AppUserOwnedObject> entities = objects.findAll { c.isAssignableFrom(it.class) }
            List active = entities.findAll { !it.archived }
            List archived = entities.findAll { it.archived }

            assert entities.size() == rwDAO.getEntitiesForUserCount(c, user)
            deepCompare(rwDAO.getEntitiesForUser(c, user, 0, 0).toList(), entities.toSet())
            assert active.size() == rwDAO.getActiveEntitiesForUserCount(c, user)
            deepCompare(rwDAO.getActiveEntitiesForUser(c, user, 0, 0).toList(), active.toSet())
            assert archived.size() == rwDAO.getArchivedEntitiesForUserCount(c, user)
            deepCompare(rwDAO.getArchivedEntitiesForUser(c, user, 0, 0).toList(), archived.toSet())
        }
    }

    @Test(groups = ["integration"])
    public void testReadEntitiesByTypePaginated() {
        initData()
        List<Class<? extends AppUserOwnedObject>> classes = [AppUserOwnedObject, Semester, Observation, Semester, Photo, Student, Observable, ClassList, AppUserSettings, TwoPhaseActivity]
        //  TODO optional tests on deleted objects
        for (Class<? extends AppUserOwnedObject> c : classes) {
            println "Checking " + c.canonicalName

            List<AppUserOwnedObject> entities = objects.findAll { c.isAssignableFrom(it.class) }
            List active = entities.findAll { !it.archived }
            List archived = entities.findAll { it.archived }
            List actuals

            actuals = retrievePaginatedData(entities.size(), { int page, int size -> rwDAO.getEntitiesForUser(c, user, page * size, size) })
            assert entities.size() == actuals.size()
            deepCompare(actuals, entities.toSet())

            actuals = retrievePaginatedData(active.size(), { int page, int size -> rwDAO.getActiveEntitiesForUser(c, user, page * size, size) })
            assert active.size() == actuals.size()
            deepCompare(actuals, active.toSet())


            actuals = retrievePaginatedData(archived.size(), { int page, int size -> rwDAO.getArchivedEntitiesForUser(c, user, page * size, size) })
            assert archived.size() == actuals.size()
            deepCompare(actuals, archived.toSet())
        }
    }

    static List retrievePaginatedData(int resultSetSize, Closure<Collection> test) {
        int divBy = 3
        def pageSize = Math.max((int) Math.round(resultSetSize / divBy), 1)
        def numPages = (resultSetSize % divBy == 0) ? divBy : (divBy + 1)

        List actuals = []
        for (int i = 0; i < numPages; ++i) {
            actuals.addAll(test(i, pageSize))
        }
        actuals
    }

    private synchronized void initData() {
        if (user != null) {
            return
        }

        String baseName = "ReadAll"
        user = createUser(baseName)

        ClassList cl1 = createClassList(user, baseName + "1")
        ClassList cl2 = createClassList(user, baseName + "2", true)
        Photo cl1p1 = createPhoto(user, cl1, baseName)
        Photo cl2p1 = createPhoto(user, cl2, baseName, true)

        Semester sem1 = createSemester(user, baseName + "1", LocalDate.now().minusMonths(6), LocalDate.now().plusMonths(1))
        Semester sem2 = createSemester(user, baseName + "1", LocalDate.now().minusMonths(12), LocalDate.now().minusMonths(6).minusDays(1), true)
        objects.add(rwDAO.get(Semester, sem1.id))
        objects.add(rwDAO.get(Semester, sem2.id))

        ObservationCategory oc1 = createCategory(user, baseName + "1", "BS1")
        ObservationCategory oc2 = createCategory(user, baseName + "2", "BS2")
        ObservationCategory oc3 = createCategory(user, baseName + "3", "BS3", true)
        objects.add(rwDAO.get(ObservationCategory, oc1.id))
        objects.add(rwDAO.get(ObservationCategory, oc2.id))
        objects.add(rwDAO.get(ObservationCategory, oc3.id))

        AppUserSettings settings = createAppUserSettings(user)
        objects.add(rwDAO.get(AppUserSettings, settings.id))

        Student student1 = createStudent(user, baseName + 1, false, [cl1] as Set)
        Student student2 = createStudent(user, baseName + 2, false, [cl1, cl2] as Set)
        Student student3 = createStudent(user, baseName + 3, false, [cl2] as Set)

        Observation s1o1 = createObservation(user, oc2, student1, baseName)
        Observation s1o2 = createObservation(user, oc1, student1, baseName, false, sem2.start.plusDays(30).toLocalDateTime(LocalTime.now()))
        Observation s1o3 = createObservation(user, oc2, student1, baseName, true, LocalDateTime.now().plusDays(1))
        Observation s1o4 = createObservation(user, null, student1, baseName)
        s1o2.addCategory(oc1)
        s1o2 = rwDAO.update(user, s1o2)
        Photo s1o2p1 = createPhoto(user, s1o2, baseName)
        Photo s1o2p2 = createPhoto(user, s1o2, baseName, true)
        Photo s1o3p1 = createPhoto(user, s1o3, baseName, true)
        objects.add(rwDAO.get(Observation, s1o1.id))
        objects.add(rwDAO.get(Observation, s1o2.id))
        objects.add(rwDAO.get(Observation, s1o3.id))
        objects.add(rwDAO.get(Observation, s1o4.id))
        objects.add(rwDAO.get(Photo, s1o2p1.id))
        objects.add(rwDAO.get(Photo, s1o2p2.id))
        objects.add(rwDAO.get(Photo, s1o3p1.id))

        Observation s2o1 = createObservation(user, oc1, student2, baseName, false, LocalDateTime.now().plusDays(50))
        Observation s2o2 = createObservation(user, oc3, student2, baseName, false, sem2.start.minusDays(3).toLocalDateTime(LocalTime.now()))
        Observation s2o3 = createObservation(user, oc3, student2, baseName, true, LocalDateTime.now().plusDays(1))
        s2o2.addCategory(oc2)
        s2o2 = rwDAO.update(user, s2o2)
        Photo s2o2p1 = createPhoto(user, s2o2, baseName)
        Photo s2o2p2 = createPhoto(user, s2o2, baseName, true)
        Photo s2o3p1 = createPhoto(user, s2o3, baseName, true)
        objects.add(rwDAO.get(Observation, s2o1.id))
        objects.add(rwDAO.get(Observation, s2o2.id))
        objects.add(rwDAO.get(Observation, s2o3.id))
        objects.add(rwDAO.get(Photo, s2o2p1.id))
        objects.add(rwDAO.get(Photo, s2o2p2.id))
        objects.add(rwDAO.get(Photo, s2o3p1.id))

        Observation s3o1 = createObservation(user, oc2, student3, baseName, false, LocalDateTime.now().plusDays(1))
        Observation s3o2 = createObservation(user, oc2, student3, baseName, false, sem2.end.minusDays(3).toLocalDateTime(LocalTime.now()))
        Observation s3o3 = createObservation(user, oc2, student3, baseName, true, LocalDateTime.now().minusDays(1))
        s3o2.addCategory(oc1)
        s3o2 = rwDAO.update(user, s3o2)
        Photo s3o2p1 = createPhoto(user, s3o2, baseName)
        Photo s3o2p2 = createPhoto(user, s3o2, baseName, true)
        Photo s3o3p1 = createPhoto(user, s3o3, baseName, true)
        objects.add(rwDAO.get(Observation, s3o1.id))
        objects.add(rwDAO.get(Observation, s3o2.id))
        objects.add(rwDAO.get(Observation, s3o3.id))
        objects.add(rwDAO.get(Photo, s3o2p1.id))
        objects.add(rwDAO.get(Photo, s3o2p2.id))
        objects.add(rwDAO.get(Photo, s3o3p1.id))

        Observation cl1o1 = createObservation(user, oc1, cl1, baseName, false, LocalDateTime.now().plusDays(1))
        Observation cl1o2 = createObservation(user, oc2, cl1, baseName, false, sem2.end.minusDays(3).toLocalDateTime(LocalTime.now()))
        Observation cl1o3 = createObservation(user, oc3, cl1, baseName, true, LocalDateTime.now().minusDays(1))
        Observation cl1o4 = createObservation(user, null, cl1, baseName, true, LocalDateTime.now().minusDays(1))
        cl1o2.addCategory(oc1)
        cl1o2 = rwDAO.update(user, cl1o2)
        Photo cl1o2p1 = createPhoto(user, s3o2, baseName)
        Photo cl1o2p2 = createPhoto(user, s3o2, baseName, true)
        Photo cl1o3p1 = createPhoto(user, s3o3, baseName, true)
        objects.add(rwDAO.get(Observation, cl1o1.id))
        objects.add(rwDAO.get(Observation, cl1o2.id))
        objects.add(rwDAO.get(Observation, cl1o3.id))
        objects.add(rwDAO.get(Observation, cl1o4.id))
        objects.add(rwDAO.get(Photo, cl1o2p1.id))
        objects.add(rwDAO.get(Photo, cl1o2p2.id))
        objects.add(rwDAO.get(Photo, cl1o3p1.id))

        TwoPhaseActivity activity1 = createActivity(user)
        TwoPhaseActivity activity2 = createActivity(user, true)
        objects.add(rwDAO.get(TwoPhaseActivity, activity1.id))
        objects.add(rwDAO.get(TwoPhaseActivity, activity2.id))

        objects.add(rwDAO.get(ClassList, cl1.id))
        objects.add(rwDAO.get(ClassList, cl2.id))
        objects.add(rwDAO.get(Photo, cl1p1.id))
        objects.add(rwDAO.get(Photo, cl2p1.id))
        objects.add(rwDAO.get(Student, student1.id))
        objects.add(rwDAO.get(Student, student2.id))
        objects.add(rwDAO.get(Student, student3.id))
    }

    private static <T extends IdObject, W> Map<T, W> createEmptyMap(Class<T> type, Closure<W> initCategory) {
        objects.findAll { (it in type) }.collectEntries(initCategory)
    }
}
