/* tslint:disable max-line-length */
import axios from 'axios';
import sinon from 'sinon';

import CodigoPostalService from './codigo-postal.service';
import { CodigoPostal } from '@/shared/model/pacientesms/codigo-postal.model';

const error = {
  response: {
    status: null,
    data: {
      type: null,
    },
  },
};

const axiosStub = {
  get: sinon.stub(axios, 'get'),
  post: sinon.stub(axios, 'post'),
  put: sinon.stub(axios, 'put'),
  patch: sinon.stub(axios, 'patch'),
  delete: sinon.stub(axios, 'delete'),
};

describe('Service Tests', () => {
  describe('CodigoPostal Service', () => {
    let service: CodigoPostalService;
    let elemDefault;

    beforeEach(() => {
      service = new CodigoPostalService();
      elemDefault = new CodigoPostal(
        123,
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
      );
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = { ...elemDefault };
        axiosStub.get.resolves({ data: returnedFromService });

        return service.find(123).then(res => {
          expect(res).toMatchObject(elemDefault);
        });
      });

      it('should not find an element', async () => {
        axiosStub.get.rejects(error);
        return service
          .find(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should create a CodigoPostal', async () => {
        const returnedFromService = { id: 123, ...elemDefault };
        const expected = { ...returnedFromService };

        axiosStub.post.resolves({ data: returnedFromService });
        return service.create({}).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not create a CodigoPostal', async () => {
        axiosStub.post.rejects(error);

        return service
          .create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a CodigoPostal', async () => {
        const returnedFromService = {
          codigo: 'BBBBBB',
          asentamiento: 'BBBBBB',
          tipoAsentamiento: 'BBBBBB',
          municipio: 'BBBBBB',
          estado: 'BBBBBB',
          ciudad: 'BBBBBB',
          codigoPostalAdministracion: 'BBBBBB',
          claveEstado: 'BBBBBB',
          claveOficina: 'BBBBBB',
          claveCP: 'BBBBBB',
          claveTipoAsentamiento: 'BBBBBB',
          claveMunicipio: 'BBBBBB',
          idAsentamientoCons: 'BBBBBB',
          zona: 'BBBBBB',
          claveCiudad: 'BBBBBB',
          ...elemDefault,
        };

        const expected = { ...returnedFromService };
        axiosStub.put.resolves({ data: returnedFromService });

        return service.update(expected).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not update a CodigoPostal', async () => {
        axiosStub.put.rejects(error);

        return service
          .update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should partial update a CodigoPostal', async () => {
        const patchObject = {
          codigo: 'BBBBBB',
          municipio: 'BBBBBB',
          estado: 'BBBBBB',
          codigoPostalAdministracion: 'BBBBBB',
          claveOficina: 'BBBBBB',
          claveCP: 'BBBBBB',
          claveTipoAsentamiento: 'BBBBBB',
          idAsentamientoCons: 'BBBBBB',
          ...new CodigoPostal(),
        };
        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = { ...returnedFromService };
        axiosStub.patch.resolves({ data: returnedFromService });

        return service.partialUpdate(patchObject).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not partial update a CodigoPostal', async () => {
        axiosStub.patch.rejects(error);

        return service
          .partialUpdate({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of CodigoPostal', async () => {
        const returnedFromService = {
          codigo: 'BBBBBB',
          asentamiento: 'BBBBBB',
          tipoAsentamiento: 'BBBBBB',
          municipio: 'BBBBBB',
          estado: 'BBBBBB',
          ciudad: 'BBBBBB',
          codigoPostalAdministracion: 'BBBBBB',
          claveEstado: 'BBBBBB',
          claveOficina: 'BBBBBB',
          claveCP: 'BBBBBB',
          claveTipoAsentamiento: 'BBBBBB',
          claveMunicipio: 'BBBBBB',
          idAsentamientoCons: 'BBBBBB',
          zona: 'BBBBBB',
          claveCiudad: 'BBBBBB',
          ...elemDefault,
        };
        const expected = { ...returnedFromService };
        axiosStub.get.resolves([returnedFromService]);
        return service.retrieve({ sort: {}, page: 0, size: 10 }).then(res => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should not return a list of CodigoPostal', async () => {
        axiosStub.get.rejects(error);

        return service
          .retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a CodigoPostal', async () => {
        axiosStub.delete.resolves({ ok: true });
        return service.delete(123).then(res => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a CodigoPostal', async () => {
        axiosStub.delete.rejects(error);

        return service
          .delete(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });
    });
  });
});
