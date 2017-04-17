package journey.forjobs.akazoo_project.model;

import java.util.ArrayList;

public class Playlist {
    private String ObjectType;
    private String DateUpdated;
    private long Duration;
    private long FanCount;
    private boolean IsOwner;
    private int ItemCount;
    private String LargePhotoUrl;
    private String Name;
    private String OwnerId;
    private String OwnerNickName;
    private String OwnerPhotoUrl;
    private String PhotoUrl;
    private String PlaylistId;
    private boolean ViewerIsFan;
    private ArrayList<Track> Items;

    public String getObjectType() {
        return ObjectType;
    }

    public void setObjectType(String objectType) {
        ObjectType = objectType;
    }

    public String getDateUpdated() {
        return DateUpdated;
    }

    public void setDateUpdated(String dateUpdated) {
        DateUpdated = dateUpdated;
    }

    public long getDuration() {
        return Duration;
    }

    public void setDuration(long duration) {
        Duration = duration;
    }

    public long getFanCount() {
        return FanCount;
    }

    public void setFanCount(long fanCount) {
        FanCount = fanCount;
    }

    public boolean isOwner() {
        return IsOwner;
    }

    public void setOwner(boolean owner) {
        IsOwner = owner;
    }

    public int getItemCount() {
        return ItemCount;
    }

    public void setItemCount(int itemCount) {
        ItemCount = itemCount;
    }

    public String getLargePhotoUrl() {
        return LargePhotoUrl;
    }

    public void setLargePhotoUrl(String largePhotoUrl) {
        LargePhotoUrl = largePhotoUrl;
    }

    public String getOwnerId() {
        return OwnerId;
    }

    public void setOwnerId(String ownerId) {
        OwnerId = ownerId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getOwnerNickName() {
        return OwnerNickName;
    }

    public void setOwnerNickName(String ownerNickName) {
        OwnerNickName = ownerNickName;
    }

    public String getOwnerPhotoUrl() {
        return OwnerPhotoUrl;
    }

    public void setOwnerPhotoUrl(String ownerPhotoUrl) {
        OwnerPhotoUrl = ownerPhotoUrl;
    }

    public String getPhotoUrl() {
        return PhotoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        PhotoUrl = photoUrl;
    }

    public String getPlaylistId() {
        return PlaylistId;
    }

    public void setPlaylistId(String playlistId) {
        PlaylistId = playlistId;
    }

    public boolean isViewerIsFan() {
        return ViewerIsFan;
    }

    public void setViewerIsFan(boolean viewerIsFan) {
        ViewerIsFan = viewerIsFan;
    }

    public ArrayList<Track> getItems() {
        return Items;
    }

    public void setItems(ArrayList<Track> items) {
        Items = items;
    }

    @Override
    public String toString() {
        return "Playlist{" +
                "ObjectType='" + ObjectType + '\'' +
                ", DateUpdated='" + DateUpdated + '\'' +
                ", Duration=" + Duration +
                ", FanCount=" + FanCount +
                ", IsOwner=" + IsOwner +
                ", ItemCount=" + ItemCount +
                ", LargePhotoUrl='" + LargePhotoUrl + '\'' +
                ", Name='" + Name + '\'' +
                ", OwnerId='" + OwnerId + '\'' +
                ", OwnerNickName='" + OwnerNickName + '\'' +
                ", OwnerPhotoUrl='" + OwnerPhotoUrl + '\'' +
                ", PhotoUrl='" + PhotoUrl + '\'' +
                ", PlaylistId='" + PlaylistId + '\'' +
                ", ViewerIsFan=" + ViewerIsFan +
                ", Items=" + Items +
                '}';
    }
}
