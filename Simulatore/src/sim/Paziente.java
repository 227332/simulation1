//////////////////////////////////////////////////////////////////-*-java-*-//
//             // Classroom code for "Tecniche di Programmazione"           //
//   #####     // (!) Giovanni Squillero <giovanni.squillero@polito.it>     //
//  ######     //                                                           //
//  ###   \    // Copying and distribution of this file, with or without    //
//   ##G  c\   // modification, are permitted in any medium without royalty //
//   #     _\  // provided this notice is preserved.                        //
//   |   _/    // This file is offered as-is, without any warranty.         //
//   |  _/     //                                                           //
//             // See: http://bit.ly/tecn-progr                             //
//////////////////////////////////////////////////////////////////////////////

package sim;

public class Paziente implements Comparable<Paziente> {
	//RICORDA: così si definisce un tipo enumerativo!!!
	public enum StatoPaziente {
		ROSSO, GIALLO, VERDE, BIANCO, IN_CURA, SALVO, NERO
	};
	
	private int id;
	private StatoPaziente stato;
	
	

	public Paziente(int id, StatoPaziente stato) {
		super();
		this.id = id;
		this.stato = stato;
	}

	@Override
	public int hashCode() {
		return id;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Paziente other = (Paziente) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public StatoPaziente getStato() {
		return stato;
	}

	public void setStato(StatoPaziente stato) {
		this.stato = stato;
	}

	public int getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Paziente [id=" + id + ", stato=" + stato + "]";
	}

	/*
	 * devo dare priorità al paziente in base al suo stato, che è un tipo enum!
	 * Per fare ciò uso il metodo ordinal() dell'enum, il quale restituisce la posizione
	 * 'its position in its enum declaration, where the initial constant is assigned an 
	 * ordinal of zero'
	 */
	@Override
	public int compareTo(Paziente arg0) {
		return Integer.compare(this.getStato().ordinal(), arg0.getStato().ordinal());
	}

}
