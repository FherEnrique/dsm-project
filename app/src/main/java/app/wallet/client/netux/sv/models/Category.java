package app.wallet.client.netux.sv.models;

public class Category {
    private String email;
    private String created;
    private String name;
    private String description;
    private String modified;

    public Category(){

    }
    public Category(String email, String created, String name, String description,String modified){
        this.email = email;
        this.created = created;
        this.name = name;
        this.description = description;
        this.modified = modified;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }
}
