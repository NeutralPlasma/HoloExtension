package eu.virtusdevelops.holoextension.leaderboards;

public class ModuleData {
    private String name;
    private String placeholder;
    private boolean updateOffline;
    private String extensionName;
    private int format;


    public ModuleData(String name, String placeholder, boolean updateOffline, String extensionName, int format){
        this.name = name;
        this.placeholder = placeholder;
        this.updateOffline = updateOffline;
        this.extensionName = extensionName;
        this.format = format;
    }



    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean isUpdateOffline() {
        return updateOffline;
    }

    public String getExtensionName() {
        return extensionName;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setExtensionName(String extensionName) {
        this.extensionName = extensionName;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }


    public void setUpdateOffline(boolean updateOffline) {
        this.updateOffline = updateOffline;
    }

    public void setFormat(int format) {
        this.format = format;
    }

    public int getFormat() {
        return format;
    }
}
