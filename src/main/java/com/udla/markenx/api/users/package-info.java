@ApplicationModule(
    displayName = "User Management",
    allowedDependencies = {
            "shared::aggregates",
            "shared::valueobjects",
            "students::events"
})
package com.udla.markenx.api.users;


import org.springframework.modulith.ApplicationModule;