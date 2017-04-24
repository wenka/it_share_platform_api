package com.wenka.domain.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

/**
 * Created by 文卡<wkwenka@gmail.com> on 2017/2/26.
 */
@MappedSuperclass
public abstract class AbstractVersionEntity extends AbstractEntity {

    @Version
    @Column(name = "VER", nullable = false)
    private Integer ver;

    public AbstractVersionEntity() {
    }

    public Integer getVer() {
        return ver;
    }

    public void setVer(Integer ver) {
        this.ver = ver;
    }

}
