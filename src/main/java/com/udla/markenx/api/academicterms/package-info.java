@ApplicationModule(
        displayName = "Term Management",
        allowedDependencies = {
                "shared::aggregates",
                "shared::valueobjects",
                "shared::exceptions",
                "shared::dtos",
                "courses::ports-incoming"
        })
package com.udla.markenx.api.academicterms;

import org.springframework.modulith.ApplicationModule;
