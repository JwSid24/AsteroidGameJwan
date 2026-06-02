module dk.sdu.mmmi.cbse.score {
    requires dk.sdu.mmmi.cbse.common;
    requires spring.web;

    provides dk.sdu.mmmi.cbse.common.services.ScoreSPI
            with dk.sdu.mmmi.cbse.score.ScoreClient;
}
