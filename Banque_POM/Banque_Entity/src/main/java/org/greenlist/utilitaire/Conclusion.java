package org.greenlist.utilitaire;

public enum Conclusion {

	// A chaque conclusion possible est associée son ID dans la base et le
	// string correspondant.

	/*
	 * INSERT into conclusionechange (ID,LIBELLE) values (1,'Termine'); 
	 * INSERT into conclusionechange (ID,LIBELLE) values (2,'Conclusion En Attente');
	 * INSERT into conclusionechange (ID,LIBELLE) values (3,'Notation'); 
	 * INSERT into conclusionechange (ID,LIBELLE) values (4,'Redéfinition'); 
	 * INSERT into conclusionechange (ID,LIBELLE) values (5,'Renégociation'); 
	 * INSERT into conclusionechange (ID,LIBELLE) values (6,'Reprise RDV');
	 */

	TERMINE(1, "Termine"), ATTENTE_CONCLUSION(2, "Conclusion En Attente"), NOTATION(3, "Notation"), REDEFINITION(4,
			"Redéfinition"), RENEGOCIATION(5, "Renégociation"), REPRISE_RDV(6, "Reprise RDV");

	private int idConclusion;
	private String libelle;

	private Conclusion(int idConclusion, String libelle) {
		this.idConclusion = idConclusion;
		this.libelle = libelle;
	}

	public int getIdConclusion() {
		return idConclusion;
	}

	public String getLibelle() {
		return libelle;
	}

}
