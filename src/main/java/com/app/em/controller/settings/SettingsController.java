package com.app.em.controller.settings;

import com.app.em.security.payload.response.MessageResponse;
import com.app.em.utils.Property;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;


@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class SettingsController
{
    private final Preferences preferences = Preferences.userRoot().node("ma-club-event-manager-webservice");
    private static final String CLUB_LOGO_PATH = "club_logo_path";
    private static final String CLUB_NAME = "club_name";


    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(value = "/property", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity setProperty(@RequestBody Property property) throws BackingStoreException
    {
        if ( property.getKey().isBlank() )
            return ResponseEntity.badRequest().body(new MessageResponse("A property key cannot be blank."));

        preferences.put(property.getKey(), property.getValue());
        preferences.flush();

        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping(value = "/property/{key}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getGeneralSettingsProperty(@PathVariable String key)
    {
        return getProperty(key);
    }

    @GetMapping(value = "/club_logo_path", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getClubLogoPathProperty()
    {
        return getProperty(CLUB_LOGO_PATH);
    }

    @GetMapping(value = "/club_name", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getClubNameProperty()
    {
        return getProperty(CLUB_NAME);
    }

    // - - - PRIVATE METHODS - - -

    private ResponseEntity getProperty(String key)
    {
        return Optional.ofNullable(preferences.get(key, null))
                .map(value -> ResponseEntity.ok( new Property(key, value) ))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
