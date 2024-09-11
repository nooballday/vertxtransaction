package transactionvertx

import groovy.transform.Immutable
import groovy.transform.ToString
import groovy.transform.builder.Builder

@ToString
@Immutable
@Builder
class Hunter {
    private String name
    private HunterClass hunterClass
    private Integer level

    String getName() {
        return name
    }

    void setName(String name) {
        this.name = name
    }

    HunterClass getHunterClass() {
        return hunterClass
    }

    void setHunterClass(HunterClass hunterClass) {
        this.hunterClass = hunterClass
    }

    Integer getLevel() {
        return level
    }

    void setLevel(Integer level) {
        this.level = level
    }

    Object asType(Class type) {
        if (type == Hunter.class) {
            return new Hunter(name, hunterClass, level)
        }
        super.asType(type)
    }
}
