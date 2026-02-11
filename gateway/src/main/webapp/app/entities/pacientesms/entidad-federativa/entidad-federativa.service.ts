import axios from 'axios';

import { type IEntidadFederativa } from '@/shared/model/pacientesms/entidad-federativa.model';

const baseApiUrl = 'services/pacientesms/api/entidad-federativas';

export default class EntidadFederativaService {
  public find(id: number): Promise<IEntidadFederativa> {
    return new Promise<IEntidadFederativa>((resolve, reject) => {
      axios
        .get(`${baseApiUrl}/${id}`)
        .then(res => {
          resolve(res.data);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  public retrieve(): Promise<any> {
    return new Promise<any>((resolve, reject) => {
      axios
        .get(baseApiUrl)
        .then(res => {
          resolve(res);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  public delete(id: number): Promise<any> {
    return new Promise<any>((resolve, reject) => {
      axios
        .delete(`${baseApiUrl}/${id}`)
        .then(res => {
          resolve(res);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  public create(entity: IEntidadFederativa): Promise<IEntidadFederativa> {
    return new Promise<IEntidadFederativa>((resolve, reject) => {
      axios
        .post(`${baseApiUrl}`, entity)
        .then(res => {
          resolve(res.data);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  public update(entity: IEntidadFederativa): Promise<IEntidadFederativa> {
    return new Promise<IEntidadFederativa>((resolve, reject) => {
      axios
        .put(`${baseApiUrl}/${entity.id}`, entity)
        .then(res => {
          resolve(res.data);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  public partialUpdate(entity: IEntidadFederativa): Promise<IEntidadFederativa> {
    return new Promise<IEntidadFederativa>((resolve, reject) => {
      axios
        .patch(`${baseApiUrl}/${entity.id}`, entity)
        .then(res => {
          resolve(res.data);
        })
        .catch(err => {
          reject(err);
        });
    });
  }
}
