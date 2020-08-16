package com.papp.skyline.actuator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class InfoActuatorContributor implements InfoContributor {

    @Autowired
    BuildProperties buildProperties;

    @Override
    public void contribute(Info.Builder builder) {
        // Contribute info actuator example
        // Map<String, String> appDetails = new HashMap<>();
        // appDetails.put("name", buildProperties.getName());
        // appDetails.put("version", buildProperties.getVersion());
    }
}
