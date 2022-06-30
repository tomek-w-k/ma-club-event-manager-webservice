package com.app.em.details;


public class CampEventDetails
{
    private final Long campEventId;
    private final Integer numberOfCampRegistrations;
    private final Integer numberOfPlaces;


    public CampEventDetails(Long campEventId, Integer numberOfCampRegistrations, Integer numberOfPlaces)
    {
        this.campEventId = campEventId;
        this.numberOfCampRegistrations = numberOfCampRegistrations;
        this.numberOfPlaces = numberOfPlaces;
    }

    public Long getCampEventId()
    {
        return campEventId;
    }

    public Integer getNumberOfCampRegistrations()
    {
        return numberOfCampRegistrations;
    }

    public Integer getNumberOfPlaces()
    {
        return numberOfPlaces;
    }
}
