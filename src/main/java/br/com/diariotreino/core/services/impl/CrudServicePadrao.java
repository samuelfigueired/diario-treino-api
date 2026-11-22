package br.com.diariotreino.core.services.impl;

import br.com.diariotreino.core.domains.mappers.BaseMapper;
import br.com.diariotreino.core.entidades.EntidadeDoUsuario;
import br.com.diariotreino.core.exception.NotFoundException;
import br.com.diariotreino.core.repositorys.RepositorioDoUsuario;
import br.com.diariotreino.core.services.CrudService;
import br.com.diariotreino.core.util.CurrentUser;

import org.springframework.transaction.annotation.Transactional;

/**
 * Template Method para cadastros do usuário. A subclasse informa repositório, mapper e nome do recurso,
 * e pode sobrescrever {@link #validar}, {@link #paraSaida} e {@link #excluir} (ex.: exclusão lógica).
 */
public abstract class CrudServicePadrao<E extends EntidadeDoUsuario, IN, OUT> implements CrudService<IN, OUT> {

    protected abstract RepositorioDoUsuario<E> repository();

    protected abstract BaseMapper<E, IN, OUT> mapper();

    /** Nome usado nas mensagens de erro ("Treino não encontrado: 10"). */
    protected abstract String recurso();

    /** Gancho chamado antes de salvar (criação e atualização): normalizações e regras de negócio. */
    protected void validar(E entidade, IN dto) {
    }

    /** Gancho de saída: sobrescreva quando o OutDTO precisa de dados de outras tabelas. */
    protected OUT paraSaida(E entidade) {
        return mapper().toDto(entidade);
    }

    @Override
    @Transactional(readOnly = true)
    public OUT buscar(Long id) {
        return paraSaida(buscarEntidade(id));
    }

    @Override
    @Transactional
    public OUT criar(IN dto) {
        E entidade = mapper().toEntidade(dto);
        validar(entidade, dto);
        return paraSaida(repository().save(entidade));
    }

    @Override
    @Transactional
    public OUT atualizar(Long id, IN dto) {
        E entidade = buscarEntidade(id);
        mapper().updateEntidade(dto, entidade);
        validar(entidade, dto);
        return paraSaida(repository().saveAndFlush(entidade));
    }

    @Override
    @Transactional
    public void excluir(Long id) {
        repository().delete(buscarEntidade(id));
    }

    /** Carrega a entidade garantindo que pertence ao usuário logado (de outro usuário: 404). */
    public E buscarEntidade(Long id) {
        return repository().findByIdAndUsuarioId(id, CurrentUser.usuarioId())
                .orElseThrow(() -> new NotFoundException(recurso(), id));
    }
}
