package cz.sm.web.prototype.springbootjsf.web;

import org.primefaces.model.chart.PieChartModel;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import javax.annotation.PostConstruct;
import java.io.Serializable;

@Component
@SessionScope
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
