## REST API documentation

[POST /club_documents](#addClubDocument)  
[GET /club_documents/{id}](#getClubDocument)  
[GET /club_documents](#getAllClubDocuments)  
[PUT /club_documents](#updateClubDocument)  
[DELETE /club_documents/{id}](#deleteClubDocument)  
[POST /exam_events](#addExamEvent)  
[GET /exam_events/{id}](#getExamEvent)  
[GET /exam_events](#getAllExamEvents)  
[PUT /exam_events](#updateExamEvent)  
[POST /camp_events](#addCampEvent)  
[GET /camp_events/{id}](#getCampEvent)  
[GET /camp_events](#getAllCampEvents)  
[PUT /camp_events](#updateCampEvent)  
[POST /tournament_events](#addTournamentEvent)  
[GET /tournament_events/{id}](#getTournamentEvent)  
[GET /tournament_events](#getAllTournamentEvents)  
[PUT /tournament_events](#updateTournamentEvent)  
[GET /events](#getAllEventsForWall)  
[DELETE /events/{id}](#removeEvent)  
[POST /camp_registrations](#addCampRegistration)  
[GET /camp_registrations/{id}](#getCampRegistrationById)  
[GET /camp_events/{campEventId}/camp_registrations](#getCampRegistrationsForCamp)  
[GET /users/{userId}/camp_registrations](#getCampRegistrationsForUser)  
[GET /camp_events/{campEventId}/clothing_sizes](#getClothingSizesForCampEvent)  
[PUT /camp_registrations](#updateCampRegistration)  
[DELETE /camp_registrations/{id}](#deleteCampRegistration)  
[GET /clothing_sizes/{clothingSizeId}/camp_registrations](#getCampRegistrationsCountForClothingSize)  
[POST /exam_registrations](#addExamRegistration)  
[GET /exam_registrations/{id}](#getExamRegistrationById)  
[GET /exam_events/{examEventId}/exam_registrations](#getExamRegistrationsForExam)  
[GET /users/{userId}/exam_registrations](#getExamRegistrationsForUser)  
[PUT /exam_registrations](#updateExamRegistration)  
[DELETE /exam_registrations/{id}](#deleteExamRegistration)  
[POST /tournament_events/{tournamentEventId}/teams](#addTeam)  
[GET /teams/{id}](#getTeam)  
[GET /tournament_events/{tournamentEventId}/teams](#getTeamsForEvent)  
[GET /user/{userId}/teams](#getAllTeamsForUser)  
[PUT /tournament_events/{tournamentEventId}/teams](#updateTeam)  
[DELETE /user/{userId}/teams/{id}](#deleteTeam)  
[POST /teams/{teamId}/tournament_registrations](#addTournamentRegistration)  
[GET /tournament_registrations/{id}](#getTournamentRegistrationById)  
[GET /tournament_events/{tournamentEventId}/tournament_registrations](#getTournamentRegistrationsForTournament)  
[GET /users/{userId}/tournament_registrations](#getTournamentRegistrationsForUser)  
[GET teams/{teamId}/tournament_registrations](#getTournamentRegistrationsForTeam)  
[GET /tournament_events/{tournamentEventId}/room_types](#getRoomTypesForTournament)  
[GET /tournament_events/{tournamentEventId}/stay_periods](#getStayPeriodsForTournament)  
[GET /tournament_events/{tournamentEventId}/weight_age_categories](#getWeightAgeCategoriesForTournament)  
[PUT /teams/{teamId}/tournament_registrations](#updateTournamentRegistration)  
[DELETE /tournament_registrations/{id}](#deleteTournamentRegistration)  
[GET /stay_periods/{stayPeriodId}/tournament_registrations](#getTournamentRegistrationsCountForStayPeriod)  
[GET /room_types/{roomTypeId}/tournament_registrations](#getTournamentRegistrationsCountForRoomType)  
[GET /weight_age_categories/{weightAgeCategoryId}/tournament_registrations](#getTournamentRegistrationsCountForWeightAgeCategory)  
[POST /auth/signin](#authenticateUser)  
[POST /auth/signup](#registerUser)  
[POST /reset_password/generate_token](#generateAndSendResetPasswordToken)  
[GET /reset_password/validate_token/{token}](#validateToken)  
[PUT /reset_password/reset_password](#resetPassword)  
[PUT /property](#setProperty)  
[GET /property/{key}](#getGeneralSettingsProperty)  
[GET /club_logo_path](#getClubLogoPathProperty)  
[GET /club_name](#getClubNameProperty)  
[POST /branch_chiefs](#addBranchChief)  
[GET /branch_chiefs/{id}](#getBranchChief)  
[GET /branch_chiefs](#getAllBranchChiefs)  
[PUT /branch_chiefs](#updateBranchChief)  
[DELETE /branch_chiefs/{id}](#deleteBranchChief)  
[POST /clubs](#addClub)  
[GET /clubs/{id}](#getClub)  
[GET /clubs](#getAllClubs)  
[PUT /clubs](#updateClub)  
[DELETE /clubs/{id}](#deleteClub)  
[POST /ranks](#addRank)  
[GET /ranks/{id}](#getRank)  
[GET /ranks](#getAllRanks)  
[PUT /ranks](#updateRank)  
[DELETE /ranks/{id}](#deleteRank)  
[GET roles/{roleName}](#getRoleByRoleName)  
[GET /roles](#getRoles)  
[POST /users](#addUser)  
[GET /users/{id}](#getUser)  
[GET /users](#getAllUsers)  
[GET roles/{roleName}/users](#getUsersForRole)  
[PUT /administrators](#manageAdminPrivileges)  
[PUT /users](#updateUser)  
[DELETE /users/{id}](#deleteUser)

<br/>


## addClubDocument

Adds a *ClubDocument* entity.

**URL** : /club_documents  
**Method** : POST  
**Auth required** : YES  
**Role** : ROLE_ADMIN

### Success responses

**Condition** :  A valid *ClubDocument* object in JSON format was provided.  
**Code** :  200 OK

### Error response

**Condition** :  No *ClubDocument* JSON object was provided or provided object was invalid.  
**Code** :  400 BAD REQUEST  
<br/>

## getClubDocument

Returns a *ClubDocument* object in JSON format for a specified `id`.

**URL** : /club_documents/{id}  
**Method** : GET  
**Auth required** : YES  
**Role** : ROLE_ADMIN

### Success responses

**Condition** :  An `id` of existing *ClubDocument* entity was provided.  
**Code** :  200 OK

### Error responses

**Condition** :  An `id` of non-existing *ClubDocument* entity was provided.  
**Code** :  404 NOT FOUND

**Condition** :  No `id` was provided.  
**Code** :  400 BAD REQUEST  
<br/>

## getAllClubDocuments

Returns an array of all *ClubDocument* objects in JSON format.

**URL** : /club_documents  
**Method** : GET  
**Auth required** : YES  
**Role** : ROLE_USER

### Success responses

**Condition** :  At least one *ClubDocument* entity exists.  
**Code** :  200 OK

### Error response

**Condition** :  No *ClubDocument* entity exist.  
**Code** :  200 OK  
**Result** :  Returns an empty JSON array.  
<br/>

## updateClubDocument

Updates a *ClubDocument* entity.

**URL** : /club_documents  
**Method** : PUT  
**Auth required** : YES  
**Role** : ROLE_ADMIN

### Success responses

**Condition** :  A valid *ClubDocument* object in JSON format was provided, and a *ClubDocument* entity with the same id exists.  
**Code** :  200 OK

### Error responses

**Condition** :  No *ClubDocument* JSON object was provided or provided object was invalid.  
**Code** :  400 BAD REQUEST

**Condition** :  A *ClubDocument* entity with id equal to provided *ClubDocument* object does not exist.  
**Code** :  404 NOT FOUND  
<br/>

## deleteClubDocument

Removes a *ClubDocument* entity of a specified `id`.

**URL** : /club_documents/{id}  
**Method** : DELETE  
**Auth required** : YES  
**Role** : ROLE_ADMIN

### Success responses

**Condition** :  A *ClubDocument* entity of a specified `id` exists.  
**Code** :  200 OK

### Error response

**Condition** :  A *ClubDocument* entity of a specified `id` does not exist.  
**Code** :  404 NOT FOUND  
<br/>

## addExamEvent

Adds an *ExamEvent* entity.

**URL** : /exam_events  
**Method** : POST  
**Auth required** : YES  
**Role** : ROLE_ADMIN

### Success responses

**Condition** :  A valid *ExamEvent* object in JSON format was provided.  
**Code** : 200 OK

### Error response

**Condition** :  No *ExamEvent* JSON object was provided or provided object was invalid.  
**Code** :  400 BAD REQUEST
<br/>

## getExamEvent

Returns an *ExamEvent* object in JSON format for a specified `id`.

**URL** : /exam_events/{id}  
**Method** : GET  
**Auth required** : YES  
**Role** : ROLE_ADMIN

### Success responses

**Condition** :  An `id` of existing *ExamEvent* entity was provided.  
**Code** :  200 OK

### Error responses

**Condition** :  An `id` of non-existing *ExamEvent* entity was provided.  
**Code** :  404 NOT FOUND

**Condition** :  No `id` was provided.  
**Code** :  400 BAD REQUEST  
<br/>

## getAllExamEvents

Returns an array of all *ExamEvent* objects in JSON format.

**URL** : /exam_events  
**Method** : GET  
**Auth required** : YES  
**Role** : ROLE_USER

### Success responses

**Condition** :  At least one *ExamEvent* entity exists.  
**Code** :  200 OK

### Error response

**Condition** :  No *ExamEvent* entity exist.  
**Code** :  200 OK  
**Result** :  Returns an empty JSON array.
<br/>

## updateExamEvent

Updates an *ExamEvent* entity.

**URL** : /exam_events  
**Method** : PUT  
**Auth required** : YES  
**Role** : ROLE_ADMIN

### Success responses

**Condition** :  A valid *ExamEvent* object in JSON format was provided, and an *ExamEvent* entity with the same id exists.  
**Code** :  200 OK

### Error responses

**Condition** :  No *ExamEvent* JSON object was provided or provided object was invalid.  
**Code** :  400 BAD REQUEST

**Condition** :  An *ExamEvent* entity with id equal to provided ExamEvent object does not exist.  
**Code** :  404 NOT FOUND  
<br/>

## addCampEvent

Adds a *CampEvent* entity.

**URL** : /camp_events  
**Method** : POST  
**Auth required** : YES  
**Role** : ROLE_ADMIN

### Success responses

**Condition** :  A valid *CampEvent* object in JSON format was provided.  
**Code** :  200 OK

### Error response

**Condition** :  No *CampEvent* JSON object was provided or provided object was invalid.  
**Code** :  400 BAD REQUEST
<br/>

## getCampEvent

Returns a *CampEvent* object in JSON format for a specified `id`.

**URL** : /camp_events/{id}  
**Method** : GET  
**Auth required** : YES  
**Role** : ROLE_ADMIN

### Success responses

**Condition** :  An `id` of existing *CampEvent* entity was provided.  
**Code** :  200 OK

### Error responses

**Condition** :  An `id` of non-existing *CampEvent* entity was provided.  
**Code** :  200 OK

**Condition** :  No `id` was provided.  
**Code** :  400 BAD REQUEST  
<br/>

## getAllCampEvents

Returns an array of all *CampEvent* objects in JSON format.

**URL** : /camp_events  
**Method** : GET  
**Auth required** : YES  
**Role** : ROLE_USER

### Success responses

**Condition** :  At least one *CampEvent* entity exists.  
**Code** :  200 OK

### Error response

**Condition** :  No *CampEvent* entity exist.  
**Code** :  200 OK  
**Result** :  Returns an empty JSON array.  
<br/>

## updateCampEvent

Updates a *CampEvent* entity.

**URL** : /camp_events  
**Method** : PUT  
**Auth required** : YES  
**Role** : ROLE_ADMIN

### Success responses

**Condition** :  A valid *CampEvent* object in JSON format was provided and *CampEvent* entity with the same id exists.  
**Code** :  200 OK

### Error responses

**Condition** :  No *CampEvent* JSON object was provided or provided object was invalid.  
**Code** :  400 BAD REQUEST

**Condition** :  A *CampEvent* entity equal to provided *CampEvent* object does not exist.  
**Code** :  404 NOT FOUND
<br/>

## addTournamentEvent

Adds a *TournamentEvent* entity.

**URL** : /tournament_events  
**Method** : POST  
**Auth required** : YES  
**Role** : ROLE_ADMIN

### Success responses

**Condition** :  A valid *TournamentEvent* object in JSON format was provided.  
**Code** :  200 OK

### Error response

**Condition** :  No *TournamentEvent* JSON object was provided or provided object was invalid.  
**Code** :  400 BAD REQUEST
<br/>

## getTournamentEvent

Returns a *TournamentEvent* object in JSON format for a specified `id`.

**URL** : /tournament_events/{id}  
**Method** : GET  
**Auth required** : YES  
**Role** : ROLE_TRAINER

### Success responses

**Condition** :  An `id` of existing *TournamentEvent* entity was provided.  
**Code** :  200 OK

### Error responses

**Condition** :  An `id` of non-existing *TournamentEvent* entity was provided.  
**Code** :  404 NOT FOUND

**Condition** :  No `id` was provided.  
**Code** :  400 BAD REQUEST  
<br/>

## getAllTournamentEvents

Returns an array of all *TournamentEvent* objects in JSON format.

**URL** : /tournament_events  
**Method** : GET  
**Auth required** : YES  
**Role** : ROLE_USER

### Success responses

**Condition** :  At least one *TournamentEvent* entity exists.  
**Code** :  200 OK

### Error response

**Condition** :  No *TournamentEvent* entity exist.  
**Code** :  200 OK  
**Result** :  Returns an empty JSON array.  
<br/>

## updateTournamentEvent

Updates a *TournamentEvent* entity.

**URL** : /tournament_events  
**Method** : PUT  
**Auth required** : YES  
**Role** : ROLE_ADMIN

### Success responses

**Condition** :  A valid *TournamentEvent* object in JSON format was provided, and a *TournamentEvent* entity with the same id exists.  
**Code** :  200 OK

### Error response

**Condition** :  No *TournamentEvent* JSON object was provided or provided object was invalid.  
**Code** :  400 BAD REQUEST

**Condition** :  A *TournamentEvent* entity with id equal to provided *TournamentEvent* object does not exist.  
**Code** :  404 NOT FOUND  
<br/>

## getAllEventsForWall

Returns an array of all *Event* objects in JSON format. Objects are sorted in descending order by *dateCreated* field.

**URL** : /events  
**Method** : GET  
**Auth required** : YES  
**Role** : ROLE_USER

### Success responses

**Condition** :  At least one *Event* entity exists.  
**Code** :  200 OK

### Error response

**Condition** :  No *Event* entity exist.   
**Code** :  200 OK  
**Result** :  Returns an empty JSON array.  
<br/>

## removeEvent

Removes an *Event* entity of a specified `id`.

**URL** : /events/{id}  
**Method** : DELETE  
**Auth required** : YES  
**Role** : ROLE_ADMIN

### Success responses

**Condition** :  An *Event* entity of a specified `id` exists.  
**Code** :  200 OK

### Error response

**Condition** :  An *Event* entity of a specified `id` does not exist.  
**Code** :  404 NOT FOUND  
<br/>

## addCampRegistration

Adds a *CampRegistration* entity.

**URL** : /camp_registrations  
**Method** : POST  
**Auth required** : YES  
**Role** : ROLE_USER

### Success responses

**Condition** :  A valid *CampRegistration* object in JSON format was provided.  
**Code** :  200 OK

### Error responses

**Condition** :  A *CampRegistration* entity for specified *User* and *CampEvent* entities already exists.  
**Code** :  409 CONFLICT

**Condition** :  A specified *CampEvent* does not exist.  
**Code** :  400 BAD REQUEST  
<br/>

## getCampRegistrationById

Returns a *CampRegistration* object in JSON format for a specified `id`.

**URL** : /camp_registrations/{id}  
**Method** : GET  
**Auth required** : YES  
**Role** : ROLE_USER

### Success responses

**Condition** :  An `id` of existing *CampRegistration* entity was provided.  
**Code** :  200 OK

### Error response

**Condition** :  An `id` of non-existing *CampRegistration* entity was provided.  
**Code** :  404 NOT FOUND

**Condition** :  No `id` was provided.  
**Code** :  400 BAD REQUEST  
<br/>

## getCampRegistrationsForCamp

Returns an array of all *CampRegistration* objects in JSON format for a *CampEvent* specified by `campEventId`.

**URL** : /camp_events/{campEventId}/camp_registrations  
**Method** : GET  
**Auth required** : YES  
**Role** : ROLE_USER

### Success responses

**Condition** :  At least one *CampRegistration* entity for a specified `campEventId` exists.  
**Code** :  200 OK

### Error response

**Condition** :  No *CampRegistration* entity for a specified `campEventId` exist.  
**Code** :  200 OK  
**Result** :  Returns an empty JSON array.  
<br/>

## getCampRegistrationsForUser

Returns an array of all *CampRegistration* objects in JSON format for a *User* specified by `userId`.

**URL** : /users/{userId}/camp_registrations  
**Method** : GET  
**Auth required** : YES  
**Role** : ROLE_USER

### Success responses

**Condition** :  At least one *CampRegistration* entity for a specified `userId` exists.  
**Code** :  200 OK

### Error response

**Condition** :  No *CampRegistration* entity for a specified `userId` exist.  
**Code** :  200 OK  
**Result** :  Returns an empty JSON array.  
<br/>

## getClothingSizesForCampEvent

Returns an array of all *ClothingSize* objects in JSON format for a *CampEvent* specified by `campEventId`.

**URL** : /camp_events/{campEventId}/clothing_sizes  
**Method** : GET  
**Auth required** : YES  
**Role** : ROLE_USER

### Success responses

**Condition** :  At least one *ClothingSize* entity for a specified `campEventId` exists.  
**Code** :  200 OK

### Error response

**Condition** :  No *ClothingSize* entity for a specified `campEventId` exist.  
**Code** :  200 OK  
**Result** :  Returns an empty JSON array.  
<br/>

## updateCampRegistration

Updates a *CampRegistration* entity.

**URL** : /camp_registrations  
**Method** : PUT  
**Auth required** : YES  
**Role** : ROLE_USER

### Success responses

**Condition** :  A valid *CampRegistration* object in JSON format was provided, and a *CampRegistration* entity with the same *id* exists.  
**Code** :  200 OK

### Error response

**Condition** :  No *CampRegistration* JSON object was provided or provided object was invalid.  
**Code** :  400 BAD REQUEST

**Condition** :  A *CampRegistration* entity with *id* equal to provided *CampRegistration* object does not exist.  
**Code** : 404 NOT FOUND    
<br/>

## deleteCampRegistration

Removes a *CampRegistration* entity of a specified `id`.

**URL** : /camp_registrations/{id}  
**Method** : DELETE  
**Auth required** : YES  
**Role** : ROLE_USER

### Success responses

**Condition** :  A *CampRegistration* entity of a specified `id` exists.  
**Code** :  200 OK

### Error response

**Condition** :  A *CampRegistration* entity of a specified `id` does not exist.  
**Code** :  404 NOT FOUND  
<br/>

## getCampRegistrationsCountForClothingSize

Returns a number of *CampRegistration* entities in JSON format for a *ClothingSize* specified by `clothingSizeId`.

**URL** : /clothing_sizes/{clothingSizeId}/camp_registrations  
**Method** : GET  
**Auth required** : YES  
**Role** : ROLE_USER

### Success responses

**Condition** :  A `clothingSizeId` of existing *ClothingSize* entity was provided.  
**Code** :  200 OK  
**Content example** :
```
{
    "clothingSizeCount": 8
}
```

### Error response

**Condition** :  A `clothingSizeId` of non-existing *ClothingSize* entity was provided.  
**Code** :  200 OK  
**Content example** :
```
{
    "clothingSizeCount": 0
}
```
<br/>

## addExamRegistration

Adds an *ExamRegistration* entity.

**URL** : /exam_registrations  
**Method** : POST  
**Auth required** : YES  
**Role** : ROLE_USER

### Success responses

**Condition** :  A valid *ExamRegistration* object in JSON format was provided.  
**Code** :  200 OK

### Error response

**Condition** :  An *ExamRegistration* entity for specified *User* and *ExamEvent* entities already exists.  
**Code** :  409 CONFLICT

**Condition** :  A specified *ExamEvent* does not exist.  
**Code** :  400 BAD REQUEST  
<br/>

## getExamRegistrationById

Returns an *ExamRegistration* object in JSON format for a specified `id`.

**URL** : /exam_registrations/{id}  
**Method** : GET  
**Auth required** : YES  
**Role** : ROLE_USER

### Success responses

**Condition** :  An `id` of existing *ExamRegistration* entity was provided.  
**Code** : 200 OK

### Error response

**Condition** :  An `id` of non-existing *ExamRegistration* entity was provided.  
**Code** :  404 NOT FOUND

**Condition** :  No `id` was provided.  
**Code** :  400 BAD REQUEST  
<br/>

## getExamRegistrationsForExam

Returns an array of all *ExamRegistration* objects in JSON format for an *ExamEvent* specified by `examEventId`.

**URL** : /exam_events/{examEventId}/exam_registrations  
**Method** : GET  
**Auth required** : YES  
**Role** : ROLE_USER

### Success responses

**Condition** :  At least one *ExamRegistration* entity for a specified `examEventId` exists.  
**Code** :  200 OK

### Error response

**Condition** :  No *ExamRegistration* entity for a specified `examEventId` exist.  
**Code** :  200 OK  
**Result** :  Returns an empty JSON array.  
<br/>

## getExamRegistrationsForUser

Returns an array of all *ExamRegistration* objects in JSON format for a *User* specified by `userId`.

**URL** : /users/{userId}/exam_registrations  
**Method** : GET  
**Auth required** : YES  
**Role** : ROLE_USER

### Success responses

**Condition** :  At least one *ExamRegistration* entity for a specified `userId` exists.  
**Code** :  200 OK

### Error response

**Condition** :  No *ExamRegistration* entity for a specified `userId` exist.  
**Code** :  200 OK  
**Result** :  Returns an empty JSON array.  
<br/>

## updateExamRegistration

Updates an *ExamRegistration* entity.

**URL** : /exam_registrations  
**Method** : PUT  
**Auth required** : YES  
**Role** : ROLE_USER

### Success responses

**Condition** :  A valid *ExamRegistration* object in JSON format was provided, and an *ExamRegistration* entity exists.  
**Code** :  200 OK

### Error responses

**Condition** :  No *ExamRegistration* JSON object was provided or provided object was invalid.  
**Code** :  400 BAD REQUEST

**Condition** :  An *ExamRegistration* entity with *id* equal to provided *ExamRegistration* object does not exist.  
**Code** :  404 NOT FOUND    
<br/>

## deleteExamRegistration

Removes an *ExamRegistration* entity of a specified `id`.

**URL** : /exam_registrations/{id}  
**Method** : DELETE  
**Auth required** : YES  
**Role** : ROLE_USER

### Success responses

**Condition** :  An *ExamRegistration* entity of a specified `id` exists.  
**Code** :  200 OK

### Error response

**Condition** :  An *ExamRegistration* entity of a specified `id` does not exist.  
**Code** :  404 NOT FOUND  
<br/>

## addTeam

Adds a *Team* entity for a *TournamentEvent* specified by a `tournamentEventId` url parameter and for a specified *trainer*.

**URL** : /tournament_events/{tournamentEventId}/teams  
**Method** : POST  
**Auth required** : YES  
**Role** : ROLE_TRAINER

### Success responses

**Condition** :  A *TournamentEvent* specified by `tournamentEventId` exists, and the *trainer* has not yet a *Team* created for this *TournamentEvent*    
**Code** :  200 OK

### Error response

**Condition** :  A *TournamentEvent* of a specified `torunamentEventId` does not exist.    
**Code** :  400 BAD REQUEST

**Condition** :  A *trainer* has already created a *Team* for this *TournamentEvent*.
**Code** :  400 BAD REQUEST  
<br/>

## getTeam

Returns a *Team* object in JSON format for a specified `id`.

**URL** : /teams/{id}  
**Method** : GET  
**Auth required** : YES  
**Role** : ROLE_TRAINER

### Success responses

**Condition** :  An `id` of existing *Team* entity was provided.  
**Code** :  200 OK

### Error response

**Condition** :  An `id` of existing *Team* entity was not provided.  
**Code** :  404 NOT FOUND
<br/>

## getTeamsForEvent

Returns an array of all *Team* objects in JSON format for a *TournamentEvent* specified by `tournamentEventId`.

**URL** : /tournament_events/{tournamentEventId}/teams  
**Method** : GET  
**Auth required** : YES  
**Role** : ROLE_TRAINER

### Success responses

**Condition** :  At least one *Team* for a *TournamentEvent* specified by `tournamentEventId` exists.  
**Code** :  200 OK

### Error response

**Condition** :  No *Team* entity for a specified *TournamentEvent* exist.  
**Code** :  200 OK  
**Result** :  Returns an empty JSON array.  
<br/>

## getAllTeamsForUser

Returns an array of *Team* objects in JSON format for *User* specified by `userId`.

**URL** : /user/{userId}/teams  
**Method** : GET  
**Auth required** : YES  
**Role** : ROLE_TRAINER

### Success responses

**Condition** :  At least one *Team* entity for a specified *User* exists.  
**Code** :  200 OK

### Error response

**Condition** :  No *Team* entity for a specified *User* exist.  
**Code** :  200 OK  
**Result** :  Returns an empty JSON array.  
<br/>

## updateTeam

Updates a *Team* entity.

**URL** : /tournament_events/{tournamentEventId}/teams  
**Method** : PUT  
**Auth required** : YES  
**Role** : ROLE_TRAINER

### Success responses

**Condition** :  A valid *Team* object in JSON format was provided, and a *Team* entity with the same *id* exists.  
**Code** :  200 OK

### Error response

**Condition** :  No *Team* JSON object was provided or provided object was invalid.  
**Code** :  400 BAD REQUEST

**Condition** :  A *Team* entity with id equal to provided *Team* object does not exist.  
**Code** :  404 NOT FOUND  
<br/>

## deleteTeam

Removes a *Team* entity of a specified `id`.

**URL** : /user/{userId}/teams/{id}  
**Method** : DELETE  
**Auth required** : YES  
**Role** : ROLE_TRAINER

### Success responses

**Condition** :  A *Team* entity of a specified `id` exists.  
**Code** :  200 OK

### Error response

**Condition** :  A *Team* entity of a specified `id` does not exist.  
**Code** :  404 NOT FOUND  
<br/>

## addTournamentRegistration

Adds a *TournamentRegistration* entity for a *Team* specified by `teamId`.

**URL** : /teams/{teamId}/tournament_registrations  
**Method** : POST  
**Auth required** : YES  
**Role** : ROLE_TRAINER

### Success responses

**Condition** :  A *Team* specified by `teamId` exists, a *TournamentEvent* exists, and the *user* has not been registered for this *tournamentEvent*.  
**Code** :  200 OK

### Error response

**Condition** :  A *TournamentEvent* or *Team* does not exist.   
**Code** :  400 BAD REQUEST  
<br/>

**Condition** :  *User* is already registered for this *TournamentEvent*.     
**Code** :  400 BAD REQUEST  
<br/>

## getTournamentRegistrationById

Returns a *TournamentRegistration* in JSON format for a specified `id`.

**URL** : /tournament_registrations/{id}  
**Method** : GET  
**Auth required** : YES  
**Role** : ROLE_TRAINER

### Success responses

**Condition** :  An `id` of existing *TournamentRegistration* was provided.  
**Code** :  200 OK

### Error response

**Condition** :  An `id` of non-existing *TournamentRegistration* was provided.  
**Code** :  400 NOT FOUND

**Condition** :  No `id` was provided.  
**Code** :  400 BAD REQUEST  
<br/>

## getTournamentRegistrationsForTournament

Returns a list of all *TournamentRegistration* objects in JSON format for a *TournamentEvent* specified by `tourmanentEventId`.

**URL** : /tournament_events/{tournamentEventId}/tournament_registrations  
**Method** : GET  
**Auth required** : YES  
**Role** : ROLE_ADMIN

### Success responses

**Condition** :  At least one *TournamentRegistration* entity exists for a specified *TournamentEvent*.  
**Code** :  200 OK

### Error response

**Condition** :  No *TournamentRegistration* entity for a specified *TournamentEvent* exist.  
**Code** :  200 OK  
**Result** :  Returns an empty JSON array.  
<br/>

## getTournamentRegistrationsForUser

Returns a list of all *TournamentRegistration* objects in JSON format for a *User* specified by `userId`.

**URL** : /users/{userId}/tournament_registrations  
**Method** : GET  
**Auth required** : YES  
**Role** : ROLE_TRAINER

### Success responses

**Condition** :  At least one *TournamentRegistration* entity for a specified *User* exists.  
**Code** :  200 OK

### Error response

**Condition** :  No *TournamentRegistration* entity for a specified *User* exist.  
**Code** :  200 OK  
**Result** :  Returns an empty JSON array.  
<br/>

## getTournamentRegistrationsForTeam

Returns a list of all *TournamentRegistration* objects in JSON format for a *Team* specified by `teamId`.

**URL** : teams/{teamId}/tournament_registrations  
**Method** : GET  
**Auth required** : YES  
**Role** : ROLE_TRAINER

### Success responses

**Condition** :  At least one *TournamentRegistration* entity for a specified *Team* exists.  
**Code** :  200 OK

### Error response

**Condition** :  No *TournamentRegistration* entity for a specified *Team* exist.  
**Code** :  200 OK  
**Result** :  Returns an empty JSON array.  
<br/>

## getRoomTypesForTournament

Returns a list of all *RoomType* objects in JSON format for a *TournamentEvent* specified by `tournamentEventId`.

**URL** : /tournament_events/{tournamentEventId}/room_types  
**Method** : GET  
**Auth required** : YES  
**Role** : ROLE_TRAINER

### Success responses

**Condition** :  At least one *RoomType* entity for specified *TournamentEvent* exists.  
**Code** :  200 OK

### Error response

**Condition** :  No *RoomType* entity for specified *TournamentEvent* exist.  
**Code** :  200 OK
**Result** :  Returns an empty JSON array.  
<br/>

## getStayPeriodsForTournament

Returns a list of all *StayPeriod* objects in JSON format for a *TournamentEvent* specified by `tournamentEventId`.

**URL** : /tournament_events/{tournamentEventId}/stay_periods  
**Method** : GET  
**Auth required** : YES  
**Role** : ROLE_TRAINER

### Success responses

**Condition** :  At least one *StayPeriod* entity for specified *TournamentEvent* exists.  
**Code** :  200 OK

### Error response

**Condition** :  No *StayPeriod* entity for specified *TournamentEvent* exist.  
**Code** :  200 OK  
**Result** :  Returns an empty JSON array.  
<br/>

## getWeightAgeCategoriesForTournament

Returns a list of all *WeightAgeCategory* objects in JSON format for a *TournamentEvent* specified by `tournamentEventId`.

**URL** : /tournament_events/{tournamentEventId}/weight_age_categories  
**Method** : GET  
**Auth required** : YES  
**Role** : ROLE_TRAINER

### Success responses

**Condition** :  At least one *WeightAgeCategory* entity for specified *TournamentEvent* exists.
**Code** :  200 OK

### Error response

**Condition** :  No *StayPeriod* entity for specified *TournamentEvent* exist.  
**Code** :  200 OK  
**Result** :  Returns an empty JSON array.  
<br/>

## updateTournamentRegistration

Updates a *TournamentRegistration* entity.

**URL** : /teams/{teamId}/tournament_registrations  
**Method** : PUT  
**Auth required** : YES  
**Role** : ROLE_TRAINER

### Success responses

**Condition** :  A valid *TournamentRegistration* object in JSON format was provided, and a *TournamentRegistration* entity with the same id exists.  
**Code** :  200 OK

### Error responses

**Condition** :  No *TournamentRegistration* JSON object was provided or provided object was invalid.  
**Code** :  400 BAD REQUEST

**Condition** :  A *TournamentRegistration* entity with id equal to provided *TournamentRegistration* object does not exist.  
**Code** :  404 NOT FOUND  
<br/>

## deleteTournamentRegistration

Removes a *TournamentRegistration* entity of a specified `id`.

**URL** : /tournament_registrations/{id}  
**Method** : DELETE  
**Auth required** : YES  
**Role** : ROLE_TRAINER

### Success responses

**Condition** :  A *TournamentRegistration* entity of a specified `id` exists.  
**Code** :  200 OK

### Error response

**Condition** :  A *TournamentRegistration* entity of a specified `id` does not exist.  
**Code** :  404 NOT FOUND  
<br/>

## getTournamentRegistrationsCountForStayPeriod

Returns a number of *TournamentRegistration* entities in JSON format for a *StayPeriod* specified by `stayPeriodId`.

**URL** : /stay_periods/{stayPeriodId}/tournament_registrations  
**Method** : GET  
**Auth required** : YES  
**Role** : ROLE_ADMIN

### Success responses

**Condition** :  A `stayPeriodId` of existing *StayPeriod* entity was provided.  
**Code** :  200 OK  
**Content example** :
```
{
    "stayPeriodCount": 8
}
```

### Error response

**Condition** :  A `stayPeriodId` of non-existing *StayPeriod* entity was provided.
**Code** :  200 OK
**Content example** :
```
{
    "stayPeriodCount": 0
}
```
<br/>

## getTournamentRegistrationsCountForRoomType

Returns a number of *TournamentRegistration* entities in JSON format for a *RoomType* specified by `stayPeriodId`.

**URL** : /room_types/{roomTypeId}/tournament_registrations  
**Method** : GET  
**Auth required** : YES  
**Role** : ROLE_ADMIN

### Success responses

**Condition** :  A `roomTypeId` of existing *RoomType* entity was provided.
**Code** :  200 OK  
**Content example** :
```
{
    "roomTypeCount": 8
}
```

### Error response

**Condition** :  A `roomTypeId` of non-existing *RoomType* entity was provided.  
**Code** :  200 OK
**Content example** :
```
{
    "roomTypeCount": 0
}
```
<br/>

## getTournamentRegistrationsCountForWeightAgeCategory

Returns a number of *TournamentRegistration* entities in JSON format for a *WeightAgeCategory* specified by `weightAgeCategoryId`.

**URL** : /weight_age_categories/{weightAgeCategoryId}/tournament_registrations  
**Method** : GET  
**Auth required** : YES  
**Role** : ROLE_ADMIN

### Success responses

**Condition** :  A `weightAgeCategoryId` of existing *WeightAgeCategory* entity was provided.  
**Code** :  200 OK  
**Content example** :
```
{
    "weightAgeCategoryCount": 8
}
```

### Error response

**Condition** :  A `weightAgeCategoryId` of non-existing *WeightAgeCategory* entity was provided.  
**Code** :  200 OK  
**Content example** :
```
{
    "weightAgeCategoryCount": 0
}
```
<br/>

## authenticateUser

Authenticates a specified *User*.

**URL** : /auth/signin  
**Method** : POST  
**Auth required** : NO  
**Content example** :
```
{
    "email": "tomek.wasik@mail.com",    
    "password": "my-password"    
}
```

### Success responses

**Condition** :  A specified *User* exists and *password* is correct.  
**Code** :  200 OK  
**Content example** :
```
{
    "accessToken": "eyJhbGciOi ... XYoHBshXVk-3w",
    "customSessionId": "c0cca3c6-980a-40ca-9738-78723b2e2619",
    "id": 118,
    "email": "tomek.wasik@mail.com",
    "roles": [
        "ROLE_USER",
        "ROLE_TRAINER",
        "ROLE_ADMIN"
    ]
}
```

### Error response

**Condition** :  A specified *User* does not exist and/or *password* is not correct.  
**Code** :  401 UNAUTHORIZED  
**Content example** :
```
{
    "timestamp": "2021-05-08T01:26:02.508+00:00",
    "status": 401,
    "error": "Unauthorized",
    "message": "",
    "path": "/auth/signin"
}
```
<br/>

## registerUser

Creates an account. A *User* entity is inserted based on a provided *SignUpRequest* object.

**URL** : /auth/signup  
**Method** : POST  
**Auth required** : NO  
**Content example** :
```
{
    "fullName": "Tomek Wąsik",
    "email": "tomek.wasik@mail.com",    
    "password": "my-password",
    "country": "Polska",
    "asTrainer": false,
    "roles": 
    [
        {
            "id": 20,
            "roleName": "ROLE_ADMIN"
        },
        {
            "id": 18,
            "roleName": "ROLE_USER"
        }        
    ]
}
```

### Success responses

**Condition** :  A provided *SignUpRequest* JSON object is valid, and the *User* entity for the specified *email* does not yet exist.  
**Code** :  200 OK  
**Content example** :
```
{
    "id": 127,
    "fullName": "Tomek Wąsik",
    "email": "tomek.wasik@mail.com",
    "password": "$2a$10$nVRr1W/TRb/QRRL3hJlOFh9hUgFWNHVdGWtAPG87Tj4vh1IVlq.",
    "country": "Polska",
    "roles": [
        {
            "id": 18,
            "roleName": "ROLE_USER"
        }
    ],
    "rank": null,
    "club": null,
    "branchChief": null
}
```
**Note** :  For security reasons, a *ROLE_ADMIN* role will be automatically removed from a provided *SignUpRequest* JSON object.

### Error response

**Condition** :  A provided *SignUpRequest* JSON object is not valid, and the *User* entity for the specified *email* already exists.  
**Code** :  500 INTERNAL SERVER ERROR  
**Content example** :
```
{
    "timestamp": "2021-05-08T01:35:51.885+00:00",
    "status": 500,
    "error": "Internal Server Error",
    "message": "",
    "path": "/auth/signup"
}
```
**Code** :  400 BAD REQUEST  
**Content example** :
```
{
    "timestamp": "2021-05-08T01:45:15.040+00:00",
    "status": 400,
    "error": "Bad Request",
    "message": "",
    "path": "/auth/signup"
}
```
<br/>

## generateAndSendResetPasswordToken

Generates a password reset token and sends it within a reset password email to an *email* specified in *PasswordResetRequest*.

**URL** : /reset_password/generate_token  
**Method** : POST  
**Auth required** : NO

### Success responses

**Condition** :  A *User* entity for a specified *email* exists.  
**Code** :  200 OK

### Error response

**Condition** :  A *User* entity for a specified *email* does not exist.  
**Code** :  404 NOT FOUND  
<br/>

## validateToken

Checks if a provided `token` exists in a database and is not expired.

**URL** : /reset_password/validate_token/{token}  
**Method** : GET  
**Auth required** : NO

### Success responses

**Condition** :  A provided `token` exists and is not expired.  
**Code** :  200 OK

### Error response

**Condition** :  A provided `token` is expired.  
**Code** :  498 TOKEN EXPIRED / INVALID

**Condition** :  A provided `token` does not exist in database.  
**Code** :  404 NOT FOUND  
<br/>

## resetPassword

Sets a new *password* specified in *NewPasswordRequest* JSON object for the *User*.

**URL** : /reset_password/reset_password  
**Method** : PUT  
**Auth required** : NO

### Success responses

**Condition** :  A *token* specified in *NewPasswordRequest* exists in a database.  
**Code** :  200 OK

### Error response

**Condition** :  A *token* specified in *NewPasswordRequest* does not exist in a database.  
**Code** :  404 NOT FOUND  
<br/>

## setProperty

Creates a new property or updates an existing one specified in a *Property* JSON object.

**URL** : /property  
**Method** : PUT  
**Auth required** : YES  
**Role** : ROLE_ADMIN  
**Content example** :
```
{
    "property_key": "property_value"
}
```

### Success responses

**Condition** :  A specified *Property* JSON object is valid and *property_key* is not blank.  
**Code** :  200 OK

### Error response

**Condition** :  A specified *Property* JSON object is not valid or *property_key* is blank.  
**Code** :  400 BAD REQUEST  
**Content example** :
```
{
    "message": "A property key cannot be blank."
}
```
<br/>

## getGeneralSettingsProperty

Returns a *Property* object in JSON format for a specified `key`.

**URL** : /property/{key}  
**Method** : GET  
**Auth required** : YES  
**Role** : ROLE_USER

### Success responses

**Condition** :  A *Property* of specified `key` exists.  
**Code** :  200 OK

### Error response

**Condition** :  A *Property* of specified `key` does not exist.
**Code** :  404 NOT FOUND  
<br/>

## getClubLogoPathProperty

Returns a *Property* object in JSON format for a *clubLogoPath* property.

**URL** : /club_logo_path  
**Method** : GET  
**Auth required** : NO

### Success responses

**Condition** :  A *clubLogoPath* property exists.
**Code** :  200 OK

### Error response

**Condition** :  A *clubLogoPath* property does not exist.
**Code** :  404 NOT FOUND  
<br/>

## getClubNameProperty

Returns a *Property* object in JSON format for a *clubName* property.

**URL** : /club_name  
**Method** : GET  
**Auth required** : NO

### Success responses

**Condition** :  A *clubName* property exists.  
**Code** :  200 OK

### Error response

**Condition** :  A *clubName* property does not exist.  
**Code** :  404 NOT FOUND  
<br/>

## addBranchChief

Adds a *BranchChief* entity.

**URL** : /branch_chiefs  
**Method** : POST  
**Auth required** : YES  
**Role** : ROLE_ADMIN

### Success responses

**Condition** :  A valid *BranchChief* object in JSON format was provided, and a specified *BranchChief* does not exist.   
**Code** :  200 OK

### Error response

**Condition** :  A specified *BranchChief* already exists.  
**Code** :  409 CONFLICT  
<br/>

**Condition** :  A provided *BranchChief* JSON object is not valid.    
**Code** :  400 BAD REQUEST  
<br/>

## getBranchChief

Returns a *BranchChief* object in JSON format for a specified `id`.

**URL** : /branch_chiefs/{id}  
**Method** : GET  
**Auth required** : YES  
**Role** : ROLE_ADMIN

### Success responses

**Condition** :  An `id` of existing *BranchChief* entity was provided.
**Code** :  200 OK

### Error response

**Condition** :  An `id` of non-existing *BranchChief* entity was provided.
**Code** :  404 NOT FOUND

**Condition** :  No `id` was provided.  
**Code** :  400 BAD REQUEST  
<br/>

## getAllBranchChiefs

Returns an array of all *BranchChief* objects in JSON format.

**URL** : /branch_chiefs  
**Method** : GET  
**Auth required** : NO

### Success responses

**Condition** :  At least one *BranchChief* entity exists.
**Code** :  200 OK

### Error response

**Condition** :  No *BranchChief* entity exist.
**Code** :  200 OK  
**Result** :  Returns an empty JSON array.  
<br/>

## updateBranchChief

Updates a *BranchChief* entity.

**URL** : /branch_chiefs  
**Method** : PUT  
**Auth required** : YES  
**Role** : ROLE_ADMIN

### Success responses

**Condition** :  A *BranchChief* entity of a specified `id` exists.  
**Code** :  200 OK

### Error responses

**Condition** :  No *BranchChief* JSON object was provided or provided object was invalid.  
**Code** :  400 BAD REQUEST

**Condition** :  A *BranchChief* entity with id equal to provided *BranchChief* object does not exist.  
**Code** :  404 NOT FOUND  
<br/>

## deleteBranchChief

Removes a *BranchChief* entity of a specified `id`.

**URL** : /branch_chiefs/{id}  
**Method** : DELETE  
**Auth required** : YES  
**Role** : ROLE_ADMIN

### Success responses

**Condition** :  A *BranchChief* entity of a specified `id` exists.  
**Code** :  200 OK

### Error response

**Condition** :  A *BranchChief* entity of a specified `id` does not exist.  
**Code** :  404 NOT FOUND  
<br/>

## addClub

Adds a *Club* entity.

**URL** : /clubs  
**Method** : POST  
**Auth required** : YES  
**Role** : ROLE_ADMIN

### Success responses

**Condition** :  A valid *Club* object in JSON format was provided, and a specified *Club* does not exist.   
**Code** :  200 OK

### Error response

**Condition** :  A specified *Club* already exists.  
**Code** :  409 CONFLICT  
<br/>

**Condition** :  A provided *Club* JSON object is not valid.    
**Code** :  400 BAD REQUEST  
<br/>

## getClub

Returns a *Club* object in JSON format for a specified `id`.

**URL** : /clubs/{id}  
**Method** : GET  
**Auth required** : YES  
**Role** : ROLE_ADMIN

### Success responses

**Condition** :  An `id` of existing *Club* entity was provided.
**Code** :  200 OK

### Error response

**Condition** :  An `id` of non-existing *Club* entity was provided.
**Code** :  404 NOT FOUND

**Condition** :  No `id` was provided.  
**Code** :  400 BAD REQUEST  
<br/>

## getAllClubs

Returns an array of all *Club* objects in JSON format.

**URL** : /clubs  
**Method** : GET  
**Auth required** : NO

### Success responses

**Condition** :  At least one *Club* entity exists.
**Code** :  200 OK

### Error response

**Condition** :  No *Club* entity exist.
**Code** :  200 OK  
**Result** :  Returns an empty JSON array.  
<br/>

## updateClub

Updates a *Club* entity.

**URL** : /clubs  
**Method** : PUT  
**Auth required** : YES  
**Role** : ROLE_ADMIN

### Success responses

**Condition** :  A *Club* entity of a specified `id` exists.  
**Code** :  200 OK

### Error responses

**Condition** :  No *Club* JSON object was provided or provided object was invalid.  
**Code** :  400 BAD REQUEST

**Condition** :  A *Club* entity with id equal to provided *Club* object does not exist.  
**Code** :  404 NOT FOUND  
<br/>

## deleteClub

Removes a *Club* entity of a specified `id`.

**URL** : /clubs/{id}  
**Method** : DELETE  
**Auth required** : YES  
**Role** : ROLE_ADMIN

### Success responses

**Condition** :  A *Club* entity of a specified `id` exists.  
**Code** :  200 OK

### Error response

**Condition** :  A *Club* entity of a specified `id` does not exist.  
**Code** :  404 NOT FOUND  
<br/>

## addRank

Adds a *Rank* entity.

**URL** : /ranks  
**Method** : POST  
**Auth required** : YES  
**Role** : ROLE_ADMIN

### Success responses

**Condition** :  A valid *Rank* object in JSON format was provided, and a specified *Rank* does not exist.   
**Code** :  200 OK

### Error response

**Condition** :  A specified *Rank* already exists.  
**Code** :  409 CONFLICT  
<br/>

**Condition** :  A provided *Rank* JSON object is not valid.    
**Code** :  400 BAD REQUEST  
<br/>

## getRank

Returns a *Rank* object in JSON format for a specified `id`.

**URL** : /ranks/{id}  
**Method** : GET  
**Auth required** : YES  
**Role** : ROLE_ADMIN

### Success responses

**Condition** :  An `id` of existing *Rank* entity was provided.
**Code** :  200 OK

### Error response

**Condition** :  An `id` of non-existing *Rank* entity was provided.
**Code** :  404 NOT FOUND

**Condition** :  No `id` was provided.  
**Code** :  400 BAD REQUEST  
<br/>

## getAllRanks

Returns an array of all *Rank* objects in JSON format.

**URL** : /ranks  
**Method** : GET  
**Auth required** : NO

### Success responses

**Condition** :  At least one *Rank* entity exists.
**Code** :  200 OK

### Error response

**Condition** :  No *Rank* entity exist.
**Code** :  200 OK  
**Result** :  Returns an empty JSON array.  
<br/>

## updateRank

Updates a *Rank* entity.

**URL** : /ranks  
**Method** : PUT  
**Auth required** : YES  
**Role** : ROLE_ADMIN

### Success responses

**Condition** :  A *Rank* entity of a specified `id` exists.  
**Code** :  200 OK

### Error responses

**Condition** :  No *Rank* JSON object was provided or provided object was invalid.  
**Code** :  400 BAD REQUEST

**Condition** :  A *Rank* entity with id equal to provided *Rank* object does not exist.  
**Code** :  404 NOT FOUND  
<br/>

## deleteRank

Removes a *Rank* entity of a specified `id`.

**URL** : /ranks/{id}  
**Method** : DELETE  
**Auth required** : YES  
**Role** : ROLE_ADMIN

### Success responses

**Condition** :  A *Rank* entity of a specified `id` exists.  
**Code** :  200 OK

### Error response

**Condition** :  A *Rank* entity of a specified `id` does not exist.  
**Code** :  404 NOT FOUND  
<br/>

## getRoleByRoleName

Returns a *Role* object in JSON format for a specified `roleName`.

**URL** : roles/{roleName}  
**Method** : GET  
**Auth required** : NO

### Success responses

**Condition** :  A *Role* entity of a specified `roleName` exists.  
**Code** :  200 OK

### Error response

**Condition** :  A *Role* entity of a specified `roleName` does not exist.  
**Code** :  404 NOT FOUND  
<br/>

## getRoles

Returns a list of all *Role* objects in JSON format.

**URL** : /roles  
**Method** : GET  
**Auth required** : YES  
**Role** : ROLE_ADMIN

### Success responses

**Condition** :  At least one *Role* entity exists.  
**Code** :  200 OK

### Error response

**Condition** :  No *Role* entity exist.  
**Code** :  200 OK  
**Result** :  Returns an empty JSON array.  
<br/>

## addUser

Adds a *User* entity.

**URL** : /users  
**Method** : POST  
**Auth required** : YES  
**Role** : ROLE_TRAINER

### Success responses

**Condition** :  A valid *User* object in JSON format was provided, and any *User* with specified *email* does not exist. Provided *User* does not contain a *ROLE_ADMIN* in *roles* array.  
**Code** :  200 OK

### Error responses

**Condition** :  A specified *email* is already taken.  
**Code** :  409 CONFLICT  
**Content example** :
```
{
    "message": "email_already_taken"
}
```

**Condition** :  Provided *User* contains a *ROLE_ADMIN* in *roles* array.
**Code** :  400 BAD REQUEST  
**Content example** :
```
{
    "message": "Remove ROLE_ADMIN from json.""
}
```

**Condition** :  An invalid *User* object in JSON format was provided.
**Code** :  500 INTERNAL SERVER ERROR  
<br/>

## getUser

Returns a *User* object in JSON format for a specified `id`.

**URL** : /users/{id}  
**Method** : GET  
**Auth required** : YES  
**Role** : ROLE_USER

### Success responses

**Condition** :  An `id` of existing *User* entity was provided.  
**Code** :  200 OK

### Error response

**Condition** :  An `id` of non-existing *User* entity was provided.  
**Code** :  404 NOT FOUND  
<br/>

## getAllUsers

Returns an array of all *User* objects in JSON format.

**URL** : /users  
**Method** : GET  
**Auth required** : YES  
**Role** : ROLE_ADMIN

### Success responses

**Condition** :   At least one *User* entity exists.  
**Code** : 200 OK

### Error response

**Condition** :  No *User* entity exist.
**Code** :  200 OK  
**Result** :  Returns an empty JSON array.  
<br/>

## getUsersForRole

Returns an array of all *User* objects in JSON format.

**URL** : roles/{roleName}/users?hasRole={yes | no}  
**Method** : GET  
**Auth required** : NO

***hasRole=yes*** :  Returns all *User* entities containing a specified `roleName`  
***hasRole=no*** :  Returns all *User* entities not containing a specified `roleName`

### Success responses

**Condition** :  A provided `roleName` corresponds to an existing *Role* entity and *hasRole* parameter is specified correctly.    
**Code** :  200 OK

### Error response

**Condition** :  A provided `roleName` does not correspond to an existing *Role* entity and/or *hasRole* parameter is not specified correctly.  
**Code** :  404 NOT FOUND  
<br/>

## manageAdminPrivileges

Allows to update a *User* entity who had previously not set *ROLE_ADMIN* role.

**URL** : /administrators  
**Method** : PUT  
**Auth required** : YES  
**Role** : ROLE_ADMIN

### Success responses

**Condition** :  A provided JSON object is valid and specified *User* entity exists.
**Code** :  200 OK

### Error response

**Condition** :  A provided JSON object is not valid and/or specified *User* entity does not exist.
**Code** :  404 NOT FOUND  
<br/>

## updateUser

Updates a *User* entity.

**URL** : /users  
**Method** : PUT  
**Auth required** : YES  
**Role** : ROLE_USER

### Success responses

**Condition** :  A valid *User* object in JSON format was provided, and any *User* with specified *email* does not exist. Provided *User* does not contain a *ROLE_ADMIN* in *roles* array.  
**Code** :

### Error responses

**Condition** :  A specified *email* is already taken.  
**Code** :  409 CONFLICT  
**Content example** :
```
{
    "message": "email_already_taken"
}
```

**Condition** :  Provided *User* contains a *ROLE_ADMIN* in *roles* array.
**Code** :  400 BAD REQUEST  
**Content example** :
```
{
    "message": "Remove ROLE_ADMIN from json.""
}
```

**Condition** :  An invalid *User* object in JSON format was provided.
**Code** :  500 INTERNAL SERVER ERROR  
<br/>

## deleteUser

Removes a *User* entity of a specified `id`.

**URL** : /users/{id}  
**Method** : DELETE  
**Auth required** : YES  
**Role** : ROLE_ADMIN

### Success responses

**Condition** :  A *User* entity of a specified `id` exists.  
**Code** :  200 OK

### Error response

**Condition** :  A *User* entity of a specified `id` does not exist.  
**Code** :  404 NOT FOUND  
<br/>
