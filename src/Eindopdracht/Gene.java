/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Eindopdracht;

/**
 *
 * @author Charlotte
 */
public class Gene extends AnalysePubMed{
    
    private int geneId;
    private String geneSymbol;
    private int tax_id;
    private String description;

    
    public Gene (int gi, String gs, int t,  String d){
    
    setGeneId(gi);
    setGeneSymbol(gs);
    setTax_id(t);
    setDescription(d);

    }
            
    /**
     * @return the geneId
     */
    public int getGeneId() {
        return geneId;
    }

    /**
     * @param geneId the geneId to set
     */
    public void setGeneId(int geneId) {
        this.geneId = geneId;
    }

    /**
     * @return the geneSymbol
     */
    public String getGeneSymbol() {
        return geneSymbol;
    }

    /**
     * @param geneSymbol the geneSymbol to set
     */
    public void setGeneSymbol(String geneSymbol) {
        this.geneSymbol = geneSymbol;
    }

    /**
     * @return the tax_id
     */
    public int getTax_id() {
        return tax_id;
    }

    /**
     * @param tax_id the tax_id to set
     */
    public void setTax_id(int tax_id) {
        this.tax_id = tax_id;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }
        
    public String toString(){
        return geneId + " " + geneSymbol + " " + tax_id +  " " + description;
    }

}
