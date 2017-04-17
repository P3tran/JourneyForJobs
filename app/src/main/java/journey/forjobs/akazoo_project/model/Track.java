package journey.forjobs.akazoo_project.model;

public class Track {
    private String ObjectType;
    private String ArtistName;
    private long TrackId;
    private String TrackName;
    private String ImageUrl;

    public String getObjectType() {
        return ObjectType;
    }

    public void setObjectType(String objectType) {
        ObjectType = objectType;
    }

    public String getArtistName() {
        return ArtistName;
    }

    public void setArtistName(String artistName) {
        ArtistName = artistName;
    }

    public long getTrackId() {
        return TrackId;
    }

    public void setTrackId(long trackId) {
        TrackId = trackId;
    }

    public String getTrackName() {
        return TrackName;
    }

    public void setTrackName(String trackName) {
        TrackName = trackName;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }
}
