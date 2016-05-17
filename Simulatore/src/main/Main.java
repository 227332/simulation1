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

package main;

import sim.Core;
import sim.Evento;
import sim.Paziente;

public class Main {

	Core simulatore;

	public static void main(String[] args) {
		Main m = new Main();
		m.stub();
	}

	protected void stub() {
		simulatore = new Core();
		
		simulatore.setMediciDisponibili(1);

		simulatore.aggiungiPaziente(new Paziente(1, Paziente.StatoPaziente.ROSSO));
		simulatore.aggiungiPaziente(new Paziente(2, Paziente.StatoPaziente.ROSSO));
		simulatore.aggiungiPaziente(new Paziente(3, Paziente.StatoPaziente.ROSSO));
		simulatore.aggiungiPaziente(new Paziente(4, Paziente.StatoPaziente.ROSSO));

		simulatore.aggiungiEvento(new Evento(10, Evento.TipoEvento.PAZIENTE_ARRIVA, 1));
		simulatore.aggiungiEvento(new Evento(11, Evento.TipoEvento.PAZIENTE_ARRIVA, 2));
		simulatore.aggiungiEvento(new Evento(12, Evento.TipoEvento.PAZIENTE_ARRIVA, 3));
		simulatore.aggiungiEvento(new Evento(13, Evento.TipoEvento.PAZIENTE_ARRIVA, 4));
		
		simulatore.simula();
		
		System.err.println("Persi:" + simulatore.getPazientiPersi());
		System.err.println("Salvati:" + simulatore.getPazientiSalvati());
	}

}
