package kr.ac.dankook.ecg_dating;

// WorldCupItem.java
public class WorldCupItem {
    private String name;
    private int imageId; // res/drawable 폴더에 있는 이미지의 ID

    public WorldCupItem(String name, int imageId) {
        this.name = name;
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public int getImageId() {
        return imageId;
    }
}

