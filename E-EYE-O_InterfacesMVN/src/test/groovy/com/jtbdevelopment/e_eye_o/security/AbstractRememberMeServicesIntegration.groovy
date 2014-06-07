package com.jtbdevelopment.e_eye_o.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
import org.testng.annotations.Test

/**
 * Date: 1/16/14
 * Time: 6:55 AM
 */
abstract class AbstractRememberMeServicesIntegration extends AbstractTestNGSpringContextTests {
    @Autowired
    PersistentTokenRepository persistentTokenRepository;

    @Test(groups = ["integration"])
    public void testCreate() {
        String series = "1"
        PersistentRememberMeToken token = new PersistentRememberMeToken(
                "USER",
                series,
                "xxxx",
                new Date()
        )
        persistentTokenRepository.createNewToken(token);
        PersistentRememberMeToken loaded = persistentTokenRepository.getTokenForSeries(series);
        assert Math.abs(token.date.time - loaded.date.time) < 1000
        assert token.series == loaded.series
        assert token.tokenValue == loaded.tokenValue
        assert token.username == loaded.username
    }

    @Test(groups = ["integration"])
    public void testUpdate() {
        String series = "2"
        String token1 = "xxxx"

        PersistentRememberMeToken token = new PersistentRememberMeToken(
                "USER",
                series,
                token1,
                new Date()
        )
        persistentTokenRepository.createNewToken(token);
        Thread.sleep(1000)
        Date newDate = new Date();
        String token2 = "yyyy"
        persistentTokenRepository.updateToken(token.series, token2, newDate)
        PersistentRememberMeToken loaded = persistentTokenRepository.getTokenForSeries(series);
        assert Math.abs(newDate.time - loaded.date.time) < 1000
        assert token.series == loaded.series
        assert token2 == loaded.tokenValue
        assert token.username == loaded.username
    }

    @Test(groups = ["integration"])
    public void testDelete() {
        String series1 = "3"
        String series2 = "4"
        String token1 = "xxxx"
        String token2 = "yyyy"
        String USER2 = "USER2"

        PersistentRememberMeToken rem1 = new PersistentRememberMeToken(
                USER2,
                series1,
                token1,
                new Date()
        )
        PersistentRememberMeToken rem2 = new PersistentRememberMeToken(
                USER2,
                series2,
                token2,
                new Date()
        )
        persistentTokenRepository.createNewToken(rem1);
        persistentTokenRepository.createNewToken(rem2);
        assert persistentTokenRepository.getTokenForSeries(series1)
        assert persistentTokenRepository.getTokenForSeries(series2)

        persistentTokenRepository.removeUserTokens(USER2)
        assert !persistentTokenRepository.getTokenForSeries(series1)
        assert !persistentTokenRepository.getTokenForSeries(series2)
    }
}
