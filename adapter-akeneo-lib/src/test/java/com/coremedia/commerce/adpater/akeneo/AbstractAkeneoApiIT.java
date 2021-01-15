package com.coremedia.commerce.adpater.akeneo;

import com.coremedia.commerce.adapter.akeneo.configuration.AkeneoApiConfigurationProperties;
import com.coremedia.commerce.adapter.akeneo.configuration.AkeneoConnectorConfiguration;
import org.junit.runner.RunWith;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {
        AkeneoConnectorConfiguration.class
})
@EnableConfigurationProperties({AkeneoApiConfigurationProperties.class})
@TestPropertySource(locations = "/test-defaults.properties")
public abstract class AbstractAkeneoApiIT {

}
