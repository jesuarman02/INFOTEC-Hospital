/* tslint:disable max-line-length */
import axios from 'axios';
import sinon from 'sinon';
import dayjs from 'dayjs';

import PacienteAlergiaService from './paciente-alergia.service';
import { DATE_FORMAT } from '@/shared/composables/date-format';
import { PacienteAlergia } from '@/shared/model/pacientems/paciente-alergia.model';

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
  describe('PacienteAlergia Service', () => {
    let service: PacienteAlergiaService;
    let elemDefault;
    let currentDate: Date;

    beforeEach(() => {
      service = new PacienteAlergiaService();
      currentDate = new Date();
      elemDefault = new PacienteAlergia(123, currentDate, 'AAAAAAA', 'LEVE');
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = { fechaDeteccion: dayjs(currentDate).format(DATE_FORMAT), ...elemDefault };
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

      it('should create a PacienteAlergia', async () => {
        const returnedFromService = { id: 123, fechaDeteccion: dayjs(currentDate).format(DATE_FORMAT), ...elemDefault };
        const expected = { fechaDeteccion: currentDate, ...returnedFromService };

        axiosStub.post.resolves({ data: returnedFromService });
        return service.create({}).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not create a PacienteAlergia', async () => {
        axiosStub.post.rejects(error);

        return service
          .create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a PacienteAlergia', async () => {
        const returnedFromService = {
          fechaDeteccion: dayjs(currentDate).format(DATE_FORMAT),
          reaccion: 'BBBBBB',
          gravedad: 'BBBBBB',
          ...elemDefault,
        };

        const expected = { fechaDeteccion: currentDate, ...returnedFromService };
        axiosStub.put.resolves({ data: returnedFromService });

        return service.update(expected).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not update a PacienteAlergia', async () => {
        axiosStub.put.rejects(error);

        return service
          .update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should partial update a PacienteAlergia', async () => {
        const patchObject = { fechaDeteccion: dayjs(currentDate).format(DATE_FORMAT), reaccion: 'BBBBBB', ...new PacienteAlergia() };
        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = { fechaDeteccion: currentDate, ...returnedFromService };
        axiosStub.patch.resolves({ data: returnedFromService });

        return service.partialUpdate(patchObject).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not partial update a PacienteAlergia', async () => {
        axiosStub.patch.rejects(error);

        return service
          .partialUpdate({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of PacienteAlergia', async () => {
        const returnedFromService = {
          fechaDeteccion: dayjs(currentDate).format(DATE_FORMAT),
          reaccion: 'BBBBBB',
          gravedad: 'BBBBBB',
          ...elemDefault,
        };
        const expected = { fechaDeteccion: currentDate, ...returnedFromService };
        axiosStub.get.resolves([returnedFromService]);
        return service.retrieve().then(res => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should not return a list of PacienteAlergia', async () => {
        axiosStub.get.rejects(error);

        return service
          .retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a PacienteAlergia', async () => {
        axiosStub.delete.resolves({ ok: true });
        return service.delete(123).then(res => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a PacienteAlergia', async () => {
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
