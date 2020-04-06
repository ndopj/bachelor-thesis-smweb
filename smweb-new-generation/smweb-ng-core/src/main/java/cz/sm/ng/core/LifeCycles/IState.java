package cz.sm.ng.core.LifeCycles;


/**
 * Toto rozhranie definuje obecny stav stavoveho automatu.
 *
 * @author Roman Stoklasa
 */
public interface IState {

    /**
     * Vykona prechod z aktualneho stavu do noveho stavu, ktory vrati ako
     * vysledok metody.
     *
     * Tato metoda vytvara novu instanciu stavu, do ktoreho sa stavovy automat
     * dostane, ak v aktualnom stave nastane zadana udalost event.
     *
     * @param event
     * @return
     */
    public IState performTransition(ITransitionEvent event);

    /**
     * Ma za ulohu vratit vlastny nazov (stavu).
     *
     * @return
     */
    public String getName();

    /**
     * Tato metoda je vyvolana zivotnym cyklom po tom, co sa dany stav stane
     * novym aktivnym stavom zivotneho cyklu (obecne, prechodoveho automatu).
     *
     * Tato metoda moze sluzit k tomu, aby stav sam o sebe nieco vykonal --
     * napr. ze stav Destroyed nastavi priznak PlaneInstance.destroyed na TRUE a
     * podobne.
     */
    public void activate();

}

