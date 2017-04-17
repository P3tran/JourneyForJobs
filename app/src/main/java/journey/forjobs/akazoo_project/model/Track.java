package journey.forjobs.akazoo_project.model;

public class Track {
    private String ObjectType;
    private long ArtistId;
    private String ArtistName;
    private String Genres;
    private boolean IsExplicit;
    private boolean IsOwner;
    private boolean IsPreview;
    private boolean IsStreamable;
    private boolean IsUserFan;
    private String ModuleId;
    private int TrackDuration;
    private long TrackId;
    private String TrackName;
    private int UserMark;
    private long AlbumId;
    private String AlbumName;
    private String DateUserAdded;
    private String ImageUrl;
    private String LargeImageUrl;
    private long ItemId;
    private int Position;
    private String StreamingModuleId;


    public String getObjectType() {
        return ObjectType;
    }

    public void setObjectType(String objectType) {
        ObjectType = objectType;
    }

    public long getArtistId() {
        return ArtistId;
    }

    public void setArtistId(long artistId) {
        ArtistId = artistId;
    }

    public String getArtistName() {
        return ArtistName;
    }

    public void setArtistName(String artistName) {
        ArtistName = artistName;
    }

    public String getGenres() {
        return Genres;
    }

    public void setGenres(String genres) {
        Genres = genres;
    }

    public boolean isExplicit() {
        return IsExplicit;
    }

    public void setExplicit(boolean explicit) {
        IsExplicit = explicit;
    }

    public boolean isOwner() {
        return IsOwner;
    }

    public void setOwner(boolean owner) {
        IsOwner = owner;
    }

    public boolean isPreview() {
        return IsPreview;
    }

    public void setPreview(boolean preview) {
        IsPreview = preview;
    }

    public boolean isUserFan() {
        return IsUserFan;
    }

    public void setUserFan(boolean userFan) {
        IsUserFan = userFan;
    }

    public boolean isStreamable() {
        return IsStreamable;
    }

    public void setStreamable(boolean streamable) {
        IsStreamable = streamable;
    }

    public String getModuleId() {
        return ModuleId;
    }

    public void setModuleId(String moduleId) {
        ModuleId = moduleId;
    }

    public int getTrackDuration() {
        return TrackDuration;
    }

    public void setTrackDuration(int trackDuration) {
        TrackDuration = trackDuration;
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

    public int getUserMark() {
        return UserMark;
    }

    public void setUserMark(int userMark) {
        UserMark = userMark;
    }

    public long getAlbumId() {
        return AlbumId;
    }

    public void setAlbumId(long albumId) {
        AlbumId = albumId;
    }

    public String getAlbumName() {
        return AlbumName;
    }

    public void setAlbumName(String albumName) {
        AlbumName = albumName;
    }

    public String getDateUserAdded() {
        return DateUserAdded;
    }

    public void setDateUserAdded(String dateUserAdded) {
        DateUserAdded = dateUserAdded;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getLargeImageUrl() {
        return LargeImageUrl;
    }

    public void setLargeImageUrl(String largeImageUrl) {
        LargeImageUrl = largeImageUrl;
    }

    public long getItemId() {
        return ItemId;
    }

    public void setItemId(long itemId) {
        ItemId = itemId;
    }

    public String getStreamingModuleId() {
        return StreamingModuleId;
    }

    public void setStreamingModuleId(String streamingModuleId) {
        StreamingModuleId = streamingModuleId;
    }

    public int getPosition() {
        return Position;
    }

    public void setPosition(int position) {
        Position = position;
    }
}
