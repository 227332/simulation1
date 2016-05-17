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
	public int getPazientiSalvati() {
		return pazientiSalvati;
	}

	public int getPazientiPersi() {
		return pazientiPersi;
	}

	int pazientiSalvati = 0;
	int pazientiPersi = 0;

	Queue<Evento> listaEventi = new PriorityQueue<Evento>();
	Map<Integer, Paziente> pazienti = new HashMap<Integer, Paziente>();
	int mediciDisponibili = 0;
	Queue<Paziente> pazientiInAttesa = new PriorityQueue<Paziente>();

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

	public void passo() {
		Evento e = listaEventi.remove();
		switch (e.getTipo()) {
		case PAZIENTE_ARRIVA:
			System.out.println("Arrivo paziente:" + e);
			pazientiInAttesa.add(pazienti.get(e.getDato()));
			switch (pazienti.get(e.getDato()).getStato()) {
			case BIANCO:
				break;
			case GIALLO:
				this.aggiungiEvento(new Evento(e.getTempo() + 6 * 60, Evento.TipoEvento.PAZIENTE_MUORE, e.getDato()));
				break;
			case ROSSO:
				this.aggiungiEvento(new Evento(e.getTempo() + 1 * 60, Evento.TipoEvento.PAZIENTE_MUORE, e.getDato()));
				break;
			case VERDE:
				this.aggiungiEvento(new Evento(e.getTempo() + 12 * 60, Evento.TipoEvento.PAZIENTE_MUORE, e.getDato()));
				break;
			default:
				System.err.println("Panik!");
			}
			break;
		case PAZIENTE_GUARISCE:
			if (pazienti.get(e.getDato()).getStato() != Paziente.StatoPaziente.NERO) {
				System.out.println("Paziente salvato: " + e);
				pazienti.get(e.getDato()).setStato(Paziente.StatoPaziente.SALVO);
				++mediciDisponibili;
				++pazientiSalvati;
			}
			break;
		case PAZIENTE_MUORE:
			if (pazienti.get(e.getDato()).getStato() == Paziente.StatoPaziente.SALVO) {
				System.out.println("Paziente gi� salvato: " + e);
			} else {
				++pazientiPersi;
				pazienti.get(e.getDato()).setStato(Paziente.StatoPaziente.NERO);
				System.out.println("Paziente morto: " + e);
				if (pazienti.get(e.getDato()).getStato() == Paziente.StatoPaziente.IN_CURA) {
					++mediciDisponibili;
				}
			}
			break;
		default:
			System.err.println("Panik!");
		}

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
			--mediciDisponibili;
			pazienti.get(p.getId()).setStato(Paziente.StatoPaziente.IN_CURA);
			aggiungiEvento(new Evento(adesso + 30, Evento.TipoEvento.PAZIENTE_GUARISCE, p.getId()));
			System.out.println("Inizio a curare: " + p);
		}

		return true;
	}

	public void simula() {
		while (!listaEventi.isEmpty()) {
			passo();
		}
	}

}
