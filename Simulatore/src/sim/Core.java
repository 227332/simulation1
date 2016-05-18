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

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

public class Core {
	
	private int pazientiSalvati = 0;
	private int pazientiPersi = 0;
	//La mia EventList deve essere una coda prioritaria perchè deve estrarmi ogni volta l'evento
	//avente tempo minore
	private Queue<Evento> listaEventi = new PriorityQueue<Evento>();
	private Map<Integer, Paziente> pazienti = new HashMap<Integer, Paziente>();
	private int mediciDisponibili = 0;
	 private Queue<Paziente> pazientiInAttesa = new PriorityQueue<Paziente>();
	
	
	
	public int getPazientiSalvati() {
		return pazientiSalvati;
	}

	public int getPazientiPersi() {
		return pazientiPersi;
	}

	public int getMediciDisponibili() {
		return mediciDisponibili;
	}

	public void setMediciDisponibili(int mediciDisponibili) {
		this.mediciDisponibili = mediciDisponibili;
	}

	public void aggiungiEvento(Evento e) {
		listaEventi.add(e);
	}

	public void aggiungiPaziente(Paziente p) {
		pazienti.put(p.getId(), p);
	}
	
	//metodo che effettua l'intera simulazione
	public void simula() {
		while (!listaEventi.isEmpty()) {
			passo();
		}
	}

	//metodo che effettua un singolo passo della simulazione
	public void passo() {
		//remove() rimuove l'evento con priorità maggiore e lo restituisce
		Evento e = listaEventi.remove();
		
		switch (e.getTipo()) {
		case PAZIENTE_ARRIVA:
			System.out.println("Arrivo paziente:" + e);//è sottinteso e.toString()
			//ricorda che e.getDato() è l'id del paziente coinvolto in tale oggetto evento
			pazientiInAttesa.add(pazienti.get(e.getDato()));
			switch (pazienti.get(e.getDato()).getStato()) {
			/*
			 * OSS: Per far aggiungere ad Eclipse automaticamente tutti i possibili case dello switch,
			 * basta scrivere solo :
			 * switch (pazienti.get(e.getDato()).getStato()) {
			 * e poi cliccare: CTRL+1 -> Add missing case statements
			 * e così ti escono tutti i casi :)
			 */
			case BIANCO:
				break;
			case GIALLO:
				this.aggiungiEvento(new Evento(e.getTempo() + 6 * 60, Evento.TipoEvento.PAZIENTE_MUORE, e.getDato()));
				break;
			case ROSSO:
				//in tale case devo schedulare il nuovo evento 'il paziente muore' che si verifica 60 minuti dopo
				this.aggiungiEvento(new Evento(e.getTempo() + 1 * 60, Evento.TipoEvento.PAZIENTE_MUORE, e.getDato()));
				break;
			case VERDE:
				this.aggiungiEvento(new Evento(e.getTempo() + 12 * 60, Evento.TipoEvento.PAZIENTE_MUORE, e.getDato()));
				break;
			//il caso NERO non si può verificare perchè non può arrivare un paziente che è già morto
			default:
				System.err.println("Panik!");
			}
			break;
		case PAZIENTE_GUARISCE:
			//se il paziente non è già morto
			if (pazienti.get(e.getDato()).getStato() != Paziente.StatoPaziente.NERO) {
				System.out.println("Paziente salvato: " + e);
				pazienti.get(e.getDato()).setStato(Paziente.StatoPaziente.SALVO);
				++mediciDisponibili;
				++pazientiSalvati;
			}
			break;
		case PAZIENTE_MUORE:
			//se il paziente non è già stato curato
			if (pazienti.get(e.getDato()).getStato() == Paziente.StatoPaziente.SALVO) {
				System.out.println("Paziente già salvato: " + e);
			} else {
				++pazientiPersi;
				pazienti.get(e.getDato()).setStato(Paziente.StatoPaziente.NERO);
				System.out.println("Paziente morto: " + e);
				//se il paziente è morto mentre era in cura sotto un medico, devo far ritornare
				//il medico disponibile
				if (pazienti.get(e.getDato()).getStato() == Paziente.StatoPaziente.IN_CURA) {
					++mediciDisponibili;
				}
			}
			break;
		default:
			System.err.println("Panik!");
		}
		//faccio un loop in cui finchè cura continua a curare
		while (cura(e.getTempo()))
			;
	}

	protected boolean cura(long adesso) {
		if (pazientiInAttesa.isEmpty())
			return false;
		if (mediciDisponibili == 0)
			return false;

		Paziente p = pazientiInAttesa.remove();
		if (p.getStato() != Paziente.StatoPaziente.NERO) {
			//riduco il numero di medici disponibili
			--mediciDisponibili;
			//RICORDA: per dare come parametro un tipo enum come IN_CURA, non puoi scrivere 
			//setStato(IN_CURA) ma devi per forza indicare tutto il path: Paziente.StatoPaziente.IN_CURA
			pazienti.get(p.getId()).setStato(Paziente.StatoPaziente.IN_CURA);
			aggiungiEvento(new Evento(adesso + 30, Evento.TipoEvento.PAZIENTE_GUARISCE, p.getId()));
			System.out.println("Inizio a curare: " + p);
		}

		return true;
	}

}
