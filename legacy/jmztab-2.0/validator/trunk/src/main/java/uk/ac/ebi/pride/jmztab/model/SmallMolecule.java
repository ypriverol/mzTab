package uk.ac.ebi.pride.jmztab.model;

import java.beans.PropertyChangeEvent;
import java.net.URI;

import static uk.ac.ebi.pride.jmztab.model.MZTabConstants.*;
import static uk.ac.ebi.pride.jmztab.model.MZTabUtils.*;

/**
 * User: Qingwei, Johannes Griss
 * Date: 30/01/13
 */
public class SmallMolecule extends MZTabRecord {
    public SmallMolecule() {
        super(MZTabColumnFactory.getInstance(Section.Small_Molecule));
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(OperationCenter.UNIT_ID)) {
            setUnitId((String) evt.getNewValue());
        } else if (evt.getPropertyName().equals(OperationCenter.POSITION)) {
            // move data to new position, old position set null.
            int oldPosition = (Integer) evt.getOldValue();
            int newPosition = (Integer) evt.getNewValue();
            Object value = getValue(oldPosition);
            addValue(oldPosition, null);
            addValue(newPosition, value);
        }
    }

    public SmallMolecule(MZTabColumnFactory factory) {
        super(factory);
    }

    @SuppressWarnings("unchecked")
    public SplitList<String> getIdentifier() {
        return getSplitList(1);
    }

    public boolean addIdentifier(String identifier) {
        if (isEmpty(identifier)) {
            return false;
        }

        SplitList<String> identifierList = getIdentifier();
        if (identifierList == null) {
            identifierList = new SplitList<String>(BAR);
            setIdentifier(identifierList);
        }
        return identifierList.add(identifier);
    }

    public void setIdentifier(SplitList<String> identifier) {
        addValue(1, identifier);
    }

    public void setIdentifier(String identifierLabel) {
        setIdentifier(parseStringList(BAR, identifierLabel));
    }

    public String getUnitId() {
        return getString(2);
    }

    public void setUnitId(String unitId) {
        addValue(2, parseString(unitId));
    }

    public String getChemicalFormula() {
        return getString(3);
    }

    public void setChemicalFormula(String chemicalFormula) {
        addValue(3, parseString(chemicalFormula));
    }

    public String getSmiles() {
        return getString(4);
    }

    public void setSmiles(String smiles) {
        addValue(4, parseString(smiles));
    }

    public String getInchiKey() {
        return getString(5);
    }

    public void setInchiKey(String inchiKey) {
        addValue(5, parseString(inchiKey));
    }

    public String getDescription() {
        return getString(6);
    }

    public void setDescription(String description) {
        addValue(6, parseString(description));
    }

    public Double getMassToCharge() {
        return getDouble(7);
    }

    public void setMassToCharge(Double massToCharge) {
        addValue(7, massToCharge);
    }

    public void setMassToCharge(String massToChargeLabel) {
        setMassToCharge(parseDouble(massToChargeLabel));
    }

    public Integer getCharge() {
        return getInteger(8);
    }

    public void setCharge(Integer charge) {
        addValue(8, charge);
    }

    public void setCharge(String chargeLabel) {
        setCharge(parseInteger(chargeLabel));
    }

    @SuppressWarnings("unchecked")
    public SplitList<Double> getRetentionTime() {
        return getSplitList(9);
    }

    public boolean addRetentionTime(Double rt) {
        if (rt == null) {
            return false;
        }

        SplitList<Double> rtList = getRetentionTime();
        if (rtList == null) {
            rtList = new SplitList<Double>(BAR);
            setRetentionTime(rtList);
        }

        return rtList.add(rt);
    }

    public boolean addRetentionTime(String rtLabel) {
        return !isEmpty(rtLabel) && addRetentionTime(parseDouble(rtLabel));
    }

    public void setRetentionTime(SplitList<Double> retentionTime) {
        addValue(9, retentionTime);
    }

    public void setRetentionTime(String retentionTimeLabel) {
        setRetentionTime(parseDoubleList(retentionTimeLabel));
    }

    public Integer getTaxid() {
        return getInteger(10);
    }

    public void setTaxid(Integer taxid) {
        addValue(10, taxid);
    }

    public void setTaxid(String taxidLabel) {
        setTaxid(parseInteger(taxidLabel));
    }

    public String getSpecies() {
        return getString(11);
    }

    public void setSpecies(String species) {
        addValue(11, parseString(species));
    }

    public String getDatabase() {
        return getString(12);
    }

    public void setDatabase(String database) {
        addValue(12, parseString(database));
    }

    public String getDatabaseVersion() {
        return getString(13);
    }

    public void setDatabaseVersion(String databaseVersion) {
        addValue(13, parseString(databaseVersion));
    }

    public Reliability getReliability() {
        return getReliability(14);
    }

    public void setReliability(Reliability reliability) {
        addValue(14, reliability);
    }

    public void setReliability(String reliabilityLabel) {
        setReliability(Reliability.findReliability(reliabilityLabel));
    }

    public URI getURI() {
        return getURI(15);
    }

    public void setURI(URI uri) {
        addValue(15, uri);
    }

    public void setURI(String uriLabel) {
        setURI(parseURI(uriLabel));
    }

    @SuppressWarnings("unchecked")
    public SplitList<SpecRef> getSpectraRef() {
        return getSplitList(16);
    }

    public boolean addSpectraRef(SpecRef specRef) {
        if (specRef == null) {
            return false;
        }

        SplitList<SpecRef> specRefs = getSpectraRef();
        if (specRefs == null) {
            specRefs = new SplitList<SpecRef>(BAR);
            setSpectraRef(specRefs);
        }

        return specRefs.add(specRef);
    }

    public void setSpectraRef(SplitList<SpecRef> spectraRef) {
        addValue(16, spectraRef);
    }

    public void setSpectraRef(Unit unit, String spectraRef) {
        setSpectraRef(parseSpecRefList(unit, spectraRef));
    }

    @SuppressWarnings("unchecked")
    public SplitList<Param> getSearchEngine() {
        return getSplitList(17);
    }

    public boolean addSearchEngineParam(Param param) {
        if (param == null) {
            return false;
        }

        SplitList<Param> params = getSearchEngine();
        if (params == null) {
            params = new SplitList<Param>(BAR);
            setSearchEngine(params);
        }

        return params.add(param);
    }

    public boolean addSearchEngineParam(String paramLabel) {
        return !isEmpty(paramLabel) && addSearchEngineParam(parseParam(paramLabel));
    }

    public void setSearchEngine(SplitList<Param> searchEngine) {
        addValue(17, searchEngine);
    }

    public void setSearchEngine(String searchEngineLabel) {
        setSearchEngine(parseParamList(searchEngineLabel));
    }

    @SuppressWarnings("unchecked")
    public SplitList<Param> getSearchEngineScore() {
        return getSplitList(18);
    }

    public boolean addSearchEngineSocreParam(Param param) {
        if (param == null) {
            return false;
        }

        SplitList<Param> params = getSearchEngineScore();
        if (params == null) {
            params = new SplitList<Param>(BAR);
            setSearchEngineScore(params);
        }

        return params.add(param);
    }

    public boolean addSearchEngineSocreParam(String paramLabel) {
        return !isEmpty(paramLabel) && addSearchEngineSocreParam(parseParam(paramLabel));
    }

    public void setSearchEngineScore(SplitList<Param> searchEngineScore) {
        addValue(18, searchEngineScore);
    }

    public void setSearchEngineScore(String searchEngineScoreLabel) {
        setSearchEngineScore(parseParamList(searchEngineScoreLabel));
    }

    @SuppressWarnings("unchecked")
    public SplitList<Modification> getModifications() {
        return getSplitList(19);
    }

    public boolean addModification(Modification modification) {
        if (modification == null) {
            return false;
        }

        SplitList<Modification> modList = getModifications();
        if (modList == null) {
            modList = new SplitList<Modification>(COMMA);
            setModifications(modList);
        }

        return modList.add(modification);
    }

    public void setModifications(SplitList<Modification> modifications) {
        addValue(19, modifications);
    }

    public void setModifications(String modificationsLabel) {
        setModifications(parseModificationList(Section.Small_Molecule, modificationsLabel));
    }

    /**
     * SML  value1  value2  value3  ...
     */
    @Override
    public String toString() {
        return Section.Small_Molecule.getPrefix() + TAB + super.toString();
    }
}
