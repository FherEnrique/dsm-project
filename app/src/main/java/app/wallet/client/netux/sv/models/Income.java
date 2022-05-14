package app.wallet.client.netux.sv.models;

public class Income {
    private String email;
    private String type;
    private String description;
    private String created;
    private String modified;
    private double amount;

    public Income(){

    }
    public Income(String email, String type, String description, String created, String modified, double amount){
        this.email = email;
        this.type = type;
        this.description = description;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
