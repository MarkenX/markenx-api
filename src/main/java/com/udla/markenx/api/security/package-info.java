@ApplicationModule(
    displayName = "Security Access",
    allowedDependencies = {
            "shared::public",
            "classroom.students::public"
})
package com.udla.markenx.api.security;


import org.springframework.modulith.ApplicationModule;