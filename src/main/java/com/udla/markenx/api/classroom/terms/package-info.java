@ApplicationModule(
        displayName = "Term Management",
        allowedDependencies = {
                "shared::aggregates",
                "shared::valueobjects",
                "shared::exceptions",
                "shared::dtos",
                "classroom.courses::ports-incoming"
        })
package com.udla.markenx.api.classroom.terms;

import org.springframework.modulith.ApplicationModule;
