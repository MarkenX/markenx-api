@ApplicationModule(
        displayName = "Term Management",
        allowedDependencies = {
                "shared::models",
                "shared::infrastructure-jpa"
        })
package com.udla.markenx.api.academicterms;

import org.springframework.modulith.ApplicationModule;
