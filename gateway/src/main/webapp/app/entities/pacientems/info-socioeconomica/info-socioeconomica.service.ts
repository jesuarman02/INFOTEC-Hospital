import axios from 'axios';

import { type IInfoSocioeconomica } from '@/shared/model/pacientems/info-socioeconomica.model';

const baseApiUrl = 'services/pacientesms/api/info-socioeconomicas';

export default class InfoSocioeconomicaService {
  public find(id: number): Promise<IInfoSocioeconomica> {
    return new Promise<IInfoSocioeconomica>((resolve, reject) => {
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

  public create(entity: IInfoSocioeconomica): Promise<IInfoSocioeconomica> {
    return new Promise<IInfoSocioeconomica>((resolve, reject) => {
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

  public update(entity: IInfoSocioeconomica): Promise<IInfoSocioeconomica> {
    return new Promise<IInfoSocioeconomica>((resolve, reject) => {
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

  public partialUpdate(entity: IInfoSocioeconomica): Promise<IInfoSocioeconomica> {
    return new Promise<IInfoSocioeconomica>((resolve, reject) => {
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
