package dk.sdu.mmmi.cbse.common.services;

public interface ScoreSPI {

    void submit(double score);

    double best();

    double last();
}
