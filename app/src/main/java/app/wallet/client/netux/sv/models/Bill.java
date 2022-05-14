package app.wallet.client.netux.sv.models;

public class Bill {
    private String email;
    private String description;
    private String category;
    private String created;
    private String modified;
    private double amount;

    public Bill(){

    }
    public Bill(String email,String description, String category, String created, String modified, double amount){
        this.email = email;
        this.description = description;
        this.category = category;
        this.created = created;
        this.modified = modified;
        this.amount = amount;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
