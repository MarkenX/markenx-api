@ApplicationModule(
    displayName = "User Management",
    allowedDependencies = {
            "shared::aggregates",
            "shared::valueobjects",
            "classroom.students::events"
})
package com.udla.markenx.api.classroom.users;


import org.springframework.modulith.ApplicationModule;