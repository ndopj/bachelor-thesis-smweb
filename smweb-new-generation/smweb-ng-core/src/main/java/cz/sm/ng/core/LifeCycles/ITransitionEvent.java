package cz.sm.ng.core.LifeCycles;


/**
 * Toto rozhranie reprezentuje udalost, ktora vyvolava prechod z jedneho stavu do druheho.
 *
 * Prechod je specifikovany triedou typu udalosti a triedou dat, ktorymi je dana udalost vybavena.
 *
 *
 * @author Roman Stoklasa
 */
public interface ITransitionEvent<TypeClass, DataClass>
{

    /**
     * Vrati identifikator typu udalosti.
     * Moze to byt napr. textove pomenovanie typu prechodu, alebo hodnota nejakeho ENUM-u
     *
     * @return
     */
    public TypeClass getType();

    /**
     * Vrati data, ktore si tato udalost nesie so sebou.
     * Mozu to byt napr. rozne parametre prechodu a pod.
     *
     * @return
     */
    public DataClass getData();


}

