package com.app.em.controller.settings;

import com.app.em.persistence.entity.settings.Setting;
import com.app.em.persistence.repository.settings.SettingRepository;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class SettingsController
{
    private static final String CLUB_LOGO_PATH = "club_logo_path";
    private static final String CLUB_NAME = "club_name";
    private static final String TERMS_AND_CONDITIONS_PL = "terms_and_conditions_pl";
    private static final String TERMS_AND_CONDITIONS_EN = "terms_and_conditions_en";
    private static final String PRIVACY_POLICY_PL = "privacy_policy_pl";
    private static final String PRIVACY_POLICY_EN = "privacy_policy_en";
    private static final String GDPR_CLAUSE_PL = "gdpr_clause_pl";
    private static final String GDPR_CLAUSE_EN = "gdpr_clause_en";

    private final SettingRepository settingRepository;


    public SettingsController(SettingRepository settingRepository) {
        this.settingRepository = settingRepository;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(value = "/property", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity setProperty(@RequestBody Setting setting)
    {
        if ( setting.getKey().isBlank() )
            return ResponseEntity.badRequest().build();

        return ResponseEntity.ok( settingRepository.save(setting) );
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping(value = "/property/{key}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getGeneralSettingsProperty(@PathVariable String key)
    {
        if ( key.isBlank() )
            return ResponseEntity.badRequest().build();

        return ResponseEntity.of( settingRepository.findById(key) );
    }

    @GetMapping(value = "/club_logo_path", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getClubLogoPathProperty()
    {
        return ResponseEntity.of( settingRepository.findById(CLUB_LOGO_PATH) );
    }

    @GetMapping(value = "/club_name", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getClubNameProperty()
    {
        return ResponseEntity.of( settingRepository.findById(CLUB_NAME) );
    }

    @GetMapping(value = "/" + TERMS_AND_CONDITIONS_PL, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getTermsAndConditionsPl()
    {
        return ResponseEntity.of( settingRepository.findById(TERMS_AND_CONDITIONS_PL) );
    }

    @GetMapping(value = "/" + TERMS_AND_CONDITIONS_EN, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getTermsAndConditionsEn()
    {
        return ResponseEntity.of( settingRepository.findById(TERMS_AND_CONDITIONS_EN) );
    }

    @GetMapping(value = "/" + PRIVACY_POLICY_PL, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getPrivacyPolicyPl()
    {
        return ResponseEntity.of( settingRepository.findById(PRIVACY_POLICY_PL) );
    }

    @GetMapping(value = "/" + PRIVACY_POLICY_EN, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getPrivacyPolicyEn()
    {
        return ResponseEntity.of( settingRepository.findById(PRIVACY_POLICY_EN) );
    }

    @GetMapping(value = "/" + GDPR_CLAUSE_PL, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getGdprClausePl()
    {
        return ResponseEntity.of( settingRepository.findById(GDPR_CLAUSE_PL) );
    }

    @GetMapping(value = "/" + GDPR_CLAUSE_EN, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getGdprClauseEn()
    {
        return ResponseEntity.of( settingRepository.findById(GDPR_CLAUSE_EN) );
    }
}
