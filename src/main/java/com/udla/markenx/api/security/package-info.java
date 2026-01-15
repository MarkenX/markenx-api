@ApplicationModule(
    displayName = "Security Access",
    allowedDependencies = {
            "shared::aggregates",
            "shared::valueobjects",
            "classroom.students::events"
})
package com.udla.markenx.api.security;


import org.springframework.modulith.ApplicationModule;