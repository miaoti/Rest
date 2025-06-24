package es.us.isa.restest.inputs.llm;

public class ParameterInfo {
    private String name;
    private String description;
    private String type;
    private String inLocation;
    private String format;
    private String regex;

    private String schemaType;
    private String schemaExample;

    // Getters / Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getInLocation() { return inLocation; }
    public void setInLocation(String inLocation) { this.inLocation = inLocation; }
    public String getFormat() { return format; }
    public void setFormat(String format) { this.format = format; }
    public String getRegex() { return regex; }
    public void setRegex(String regex) { this.regex = regex; }

    public String getSchemaType() { return schemaType; }
    public void setSchemaType(String schemaType) { this.schemaType = schemaType; }
    public String getSchemaExample() { return schemaExample; }
    public void setSchemaExample(String schemaExample) { this.schemaExample = schemaExample; }

    @Override
    public String toString() {
        return "ParameterInfo{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", format='" + format + '\'' +
                ", inLocation='" + inLocation + '\'' +
                ", regex='" + regex + '\'' +
                ", description='" + description + '\'' +
                ", schemaType='" + schemaType + '\'' +
                ", schemaExample='" + schemaExample + '\'' +
                '}';
    }
}
