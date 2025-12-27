@ApplicationModule(
    displayName = "User Management",
    allowedDependencies = {
            "shared::aggregates",
            "shared::valueobjects"
})
package com.udla.markenx.api.users;


import org.springframework.modulith.ApplicationModule;