package cz.sm.web.prototype.javaeejsf;

import org.primefaces.model.chart.PieChartModel;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

/**
 * Simple JSF Managed Bean which is used by JSF pages to back
 * functionality of chart pie model from PrimeFaces library.
 * This Bean is session scoped, each session will be provided
 * with its own instance of this Bean. This class is indented to
 * be used only from JSF pages.
 *
 * @author Norbert Dopjera
 */
@Named
@SessionScoped
public class ChartView implements Serializable {

    private PieChartModel livePieModel;
    private Integer candidate1 = 0;
    private Integer candidate2 = 0;

    @PostConstruct
    public void init() {
        candidate1 = 10;
        candidate2 = 10;
        createLivePieModel();
    }

    private void createLivePieModel() {
        livePieModel = new PieChartModel();
        synchronizePie();
    }

    public Integer getCandidate1() { return candidate1; }
    public void setCandidate1(Integer candidate1) { this.candidate1 = candidate1; }

    public Integer getCandidate2() { return candidate2; }
    public void setCandidate2(Integer candidate2) { this.candidate2 = candidate2; }

    public void setLivePieModel(PieChartModel livePieModel) { this.livePieModel = livePieModel; }
    public PieChartModel getLivePieModel() { return livePieModel; }

    public void synchronizePie() {
        livePieModel.set("Candidate 1", candidate1);
        livePieModel.set("Candidate 2", candidate2);
    }
}
