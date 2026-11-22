package br.com.diariotreino.core.config;

import br.com.diariotreino.core.entidades.Exercicio;
import br.com.diariotreino.core.exercicio.repositorys.ExercicioRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/** Cria o catálogo padrão de exercícios na primeira subida (em todos os ambientes). */
@Slf4j
@Component
@Order(1)
@RequiredArgsConstructor
public class CatalogoExercicios implements ApplicationRunner {

    /** Nome e grupo muscular, nesta ordem (os ids seguem a ordem da lista). */
    static final List<String[]> CATALOGO = List.of(
            new String[] {"Supino reto com barra", "Peito"},
            new String[] {"Supino inclinado com halteres", "Peito"},
            new String[] {"Crucifixo na máquina", "Peito"},
            new String[] {"Tríceps na polia com corda", "Tríceps"},
            new String[] {"Tríceps testa", "Tríceps"},
            new String[] {"Puxada frontal", "Costas"},
            new String[] {"Remada curvada", "Costas"},
            new String[] {"Remada baixa", "Costas"},
            new String[] {"Rosca direta com barra", "Bíceps"},
            new String[] {"Rosca martelo", "Bíceps"},
            new String[] {"Agachamento livre", "Pernas"},
            new String[] {"Leg press 45°", "Pernas"},
            new String[] {"Cadeira extensora", "Pernas"},
            new String[] {"Mesa flexora", "Pernas"},
            new String[] {"Panturrilha em pé", "Pernas"},
            new String[] {"Desenvolvimento com halteres", "Ombros"},
            new String[] {"Elevação lateral", "Ombros"},
            new String[] {"Prancha abdominal", "Abdômen"});

    private final ExercicioRepository repository;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (repository.countByUsuarioIdIsNull() > 0) return;
        for (String[] item : CATALOGO) {
            Exercicio e = new Exercicio();
            e.setNome(item[0]);
            e.setGrupoMuscular(item[1]);
            repository.save(e);
        }
        log.info("Catálogo de exercícios criado: {} exercícios", CATALOGO.size());
    }
}
