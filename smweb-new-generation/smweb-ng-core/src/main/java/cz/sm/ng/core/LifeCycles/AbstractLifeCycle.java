package cz.sm.ng.core.LifeCycles;


import cz.sm.ng.core.LifeCycles.exceptions.IllegalTransitionException;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Tato abstraktna trieda v sebe implementuje spolocne metody a atributy,
 * ktore su vyuzivane vsetkymi zivotnymi cyklami.
 *
 * @author Roman Stoklasa
 */
@MappedSuperclass
public abstract class AbstractLifeCycle<_StateClass extends IState, _TransitionEventClass extends ITransitionEvent>
{


//////////////////////////////////////////////////////////////////////////////////
// ============= [  A T T R I B U T E S   ] ======================================
//////////////////////////////////////////////////////////////////////////////////


    /**
     * Instancia aktualneho stavu
     */
    //@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    //private _StateClass currentState;



    /**
     * Historia stavov (pre kontrolu, sledovanie historie, a uchovavanie aktualneho stavu (= ten najnovsi)).
     *
     * POZOR!! Je treba dat pozor, ci bude Hibernate spravne zoradovat polozky, pretoze @OrderColumn nefunguje
     * (asi v kombinacii s @OneToMany)! :(  Ak by nastala situacia, ze budu niekedy stavy poprehadzovane, tak treba
     * radit stavy v pamati (pomocou @OrderBy a ID-cka stavov)
     */
    @OneToMany(mappedBy = "parentStateMachine", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    //@OrderColumn(name="historyOrder")
    @OrderBy
    private List<_StateClass> historyOfStates;


    /**
     * Instancia loggeru, pomocou ktoreho sa budu logovat chyby v zivotnych cykloch.
     */
    private static final Logger logger = Logger.getLogger(AbstractLifeCycle.class.getName());



//////////////////////////////////////////////////////////////////////////////////
// ========== [  M E T H O D S   ] ===============================================
//////////////////////////////////////////////////////////////////////////////////



    /**
     * Zakladny bezparametricky konstruktor - inicializuje zakladne atributy.
     */
    public AbstractLifeCycle()
    {
        this.historyOfStates = new LinkedList<_StateClass>();
    }



// ======================================================================================


    /**
     * Aplikuje zadany event na aktualny stav stavoveho automatu.
     *
     * Tato metoda je hlavnym nastrojom, ako "posunut" stavovy automat dopredu.
     *
     * @param event
     * @throws IllegalTransitionException
     */
    public void applyEvent(_TransitionEventClass event) throws IllegalTransitionException
    {
        try {
            IState newState = this.getCurrentState().performTransition(event);

            if (newState != this.getCurrentState()) {	// <-- zamerne - testujem zhodnost instancii!
                logger.log(Level.INFO, "{0} - PRECHOD zo stavu: {1} --> {3} [event: {2}]", new Object[]{this.getClass().getSimpleName(), this.getCurrentState().toString(), event.toString(), newState.toString()});
                this.setCurrentState((_StateClass)newState);
                newState.activate();
                //this.historyOfStates.add(this.getCurrentState());
            }

        } catch (IllegalTransitionException ex) {
            String currentStateToString = (this.getCurrentState() != null ? this.getCurrentState().toString() : "currentState=<null>");
            String eventToString = (event != null ? event.toString() : "event=<null>");
            String eventDataString = (event != null && event.getData() != null ? event.getData().toString() : "eventData=<null>");

            logger.log(Level.SEVERE, "{0} - Nastala vynimka pri pokuse o prechod zo stavu {1} pod udalostou {2} /{3}/", new Object[]{this.getClass().getSimpleName(), currentStateToString, eventDataString, eventToString});
        }

    }


// ======================================================================================


    /**
     * Vrati aktualny stav zivotneho cyklu.
     *
     * Nacitava ho z historie stavov.
     *
     * @return
     */
    public _StateClass getCurrentState()
    {
        //return currentState;
        if (this.historyOfStates == null || this.historyOfStates.isEmpty()) {
            return null;
        }

        return this.historyOfStates.get(this.historyOfStates.size() -1);
    }


    /**
     * Nastavi zadany stav ako aktualny s tym, ze predosly stav sa ulozi do historie.
     *
     * Tato metoda v podstate prida novy stav na koniec historie, cim bude povazovany za aktualny stav.
     *
     * @param currentState
     */
    protected void setCurrentState(_StateClass currentState)
    {
        this.historyOfStates.add(currentState);
    }


// ======================================================================================


    /**
     * Nastavi zadany stav ako aktualny s tym, ze predosly stav NAHRADI.
     *
     * Tato metoda v podstate nahradi stav na "vrchole" historie.
     *
     * @param currentState
     */
    protected void replaceCurrentState(_StateClass currentState)
    {
        if (this.historyOfStates.isEmpty()) {
            this.historyOfStates.add(currentState);
        } else {
            int lastIndex = this.historyOfStates.size() - 1;
            this.historyOfStates.set(lastIndex, currentState);
        }
    }


// ======================================================================================

    /**
     * Vrati zoznam historie stavov.
     * @return
     */
    public List<_StateClass> getHistoryOfStates()
    {
        return historyOfStates;
    }


    protected void setHistoryOfStates(List<_StateClass> historyOfStates)
    {
        this.historyOfStates = historyOfStates;
    }


// ======================================================================================

    /**
     * Vlozi stav do historie.
     *
     * @param state
     */
    protected void pushToHistory(_StateClass state)
    {
        this.historyOfStates.add(state);
    }

// ======================================================================================

    /**
     * Vrati posledny stav z historie a zaroven ho z historie odstrani (z definicie history listu je to vlastne aktualny stav!)
     *
     * @return Vracia NULL, ak ziadny stav v historii nie je.
     */
    protected _StateClass popFromHistory()
    {
        if (historyOfStates.isEmpty()) {
            return null;
        }
        int lastIndex = this.historyOfStates.size() - 1;
        _StateClass result = this.historyOfStates.get(lastIndex);
        this.historyOfStates.remove(lastIndex);

        return result;
    }

// ======================================================================================

    /**
     * Vrati posledny stav z historie ale neostranuje ho (z definicie history listu je to vlastne aktualny stav!)
     *
     * @param state
     * @return Vracia NULL, ak ziadny stav v historii nie je.
     */
    protected _StateClass peakFromHistory()
    {
        if (historyOfStates.isEmpty()) {
            return null;
        }
        int lastIndex = this.historyOfStates.size() - 1;
        return this.historyOfStates.get(lastIndex);
    }

// ======================================================================================

    /**
     * Vrati predosly stav (cize stav pred tym aktualnym, ak existuje (NULL ak ziadny predosly stav neexistuje).
     *
     * @return Vrati predosly stav, alebo NULL, ak predosly stav v historii nie je.
     */
    public _StateClass getPreviousState()
    {
        if (historyOfStates.size() < 2) {
            return null;
        }
        int previousStateIndex = this.historyOfStates.size() - 2;
        return this.historyOfStates.get(previousStateIndex);
    }

// ======================================================================================





}

