package com.es.core.quickOrder;

import java.util.ArrayList;
import java.util.List;

public class QuickOrderElementsDto {
    private List<QuickOrderElement> quickOrderElements;

    public QuickOrderElementsDto() {
        this.quickOrderElements = new ArrayList<QuickOrderElement>();
    }

    public List<QuickOrderElement> getQuickOrderElements() {
        return quickOrderElements;
    }

    public void setQuickOrderElements(List<QuickOrderElement> quickOrderElements) {
        this.quickOrderElements = quickOrderElements;
    }
}
