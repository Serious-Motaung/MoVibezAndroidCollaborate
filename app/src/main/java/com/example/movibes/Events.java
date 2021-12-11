package com.example.movibes;

public class Events
{
    public String EventID,ProfileID,EventVibes,EventReviews, DatePosted,EventStartDate,
            EventEndDate,Description,Venue,Notes,LineUp,EntranceFee,ImageUrl,ProfilePic;

    public Events()
    {

    }
    public Events(String description,String venue,String datePosted)
    {
        this.Description = description;
        this.Venue = venue;
        this.DatePosted = datePosted;
    }
    public String getEventID() {
        return EventID;
    }

    public String getProfilePic() {
        return ProfilePic;
    }

    public void setProfilePic(String profilePic) {
        ProfilePic = profilePic;
    }

    public void setEventID(String eventID) {
        EventID = eventID;
    }

    public String getProfileID() {
        return ProfileID;
    }

    public void setProfileID(String profileID) {
        ProfileID = profileID;
    }

    public String getEventVibes() {
        return EventVibes;
    }

    public void setEventVibes(String eventVibes) {
        EventVibes = eventVibes;
    }

    public String getEventReviews() {
        return EventReviews;
    }

    public void setEventReviews(String eventReviews) {
        EventReviews = eventReviews;
    }

    public String getDatePosted() {
        return DatePosted;
    }

    public void setDatePosted(String datePosted) {
        DatePosted = datePosted;
    }

    public String getEventStartDate() {
        return EventStartDate;
    }

    public void setEventStartDate(String eventStartDate) {
        EventStartDate = eventStartDate;
    }

    public String getEventEndDate() {
        return EventEndDate;
    }

    public void setEventEndDate(String eventEndDate) {
        EventEndDate = eventEndDate;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getVenue() {
        return Venue;
    }

    public void setVenue(String venue) {
        Venue = venue;
    }

    public String getNotes() {
        return Notes;
    }

    public void setNotes(String notes) {
        Notes = notes;
    }

    public String getLineUp() {
        return LineUp;
    }

    public void setLineUp(String lineUp) {
        LineUp = lineUp;
    }

    public String getEntranceFee() {
        return EntranceFee;
    }

    public void setEntranceFee(String entranceFee) {
        EntranceFee = entranceFee;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }
}
