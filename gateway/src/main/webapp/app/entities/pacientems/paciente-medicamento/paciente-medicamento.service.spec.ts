/* tslint:disable max-line-length */
import axios from 'axios';
import sinon from 'sinon';
import dayjs from 'dayjs';

import PacienteMedicamentoService from './paciente-medicamento.service';
import { DATE_FORMAT } from '@/shared/composables/date-format';
import { PacienteMedicamento } from '@/shared/model/pacientems/paciente-medicamento.model';

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
  describe('PacienteMedicamento Service', () => {
    let service: PacienteMedicamentoService;
    let elemDefault;
    let currentDate: Date;

    beforeEach(() => {
      service = new PacienteMedicamentoService();
      currentDate = new Date();
      elemDefault = new PacienteMedicamento(123, 'AAAAAAA', 'AAAAAAA', currentDate, currentDate, false);
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = {
          fechaInicio: dayjs(currentDate).format(DATE_FORMAT),
          fechaFin: dayjs(currentDate).format(DATE_FORMAT),
          ...elemDefault,
        };
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

      it('should create a PacienteMedicamento', async () => {
        const returnedFromService = {
          id: 123,
          fechaInicio: dayjs(currentDate).format(DATE_FORMAT),
          fechaFin: dayjs(currentDate).format(DATE_FORMAT),
          ...elemDefault,
        };
        const expected = { fechaInicio: currentDate, fechaFin: currentDate, ...returnedFromService };

        axiosStub.post.resolves({ data: returnedFromService });
        return service.create({}).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not create a PacienteMedicamento', async () => {
        axiosStub.post.rejects(error);

        return service
          .create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a PacienteMedicamento', async () => {
        const returnedFromService = {
          dosis: 'BBBBBB',
          frecuencia: 'BBBBBB',
          fechaInicio: dayjs(currentDate).format(DATE_FORMAT),
          fechaFin: dayjs(currentDate).format(DATE_FORMAT),
          activo: true,
          ...elemDefault,
        };

        const expected = { fechaInicio: currentDate, fechaFin: currentDate, ...returnedFromService };
        axiosStub.put.resolves({ data: returnedFromService });

        return service.update(expected).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not update a PacienteMedicamento', async () => {
        axiosStub.put.rejects(error);

        return service
          .update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should partial update a PacienteMedicamento', async () => {
        const patchObject = { fechaFin: dayjs(currentDate).format(DATE_FORMAT), ...new PacienteMedicamento() };
        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = { fechaInicio: currentDate, fechaFin: currentDate, ...returnedFromService };
        axiosStub.patch.resolves({ data: returnedFromService });

        return service.partialUpdate(patchObject).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not partial update a PacienteMedicamento', async () => {
        axiosStub.patch.rejects(error);

        return service
          .partialUpdate({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of PacienteMedicamento', async () => {
        const returnedFromService = {
          dosis: 'BBBBBB',
          frecuencia: 'BBBBBB',
          fechaInicio: dayjs(currentDate).format(DATE_FORMAT),
          fechaFin: dayjs(currentDate).format(DATE_FORMAT),
          activo: true,
          ...elemDefault,
        };
        const expected = { fechaInicio: currentDate, fechaFin: currentDate, ...returnedFromService };
        axiosStub.get.resolves([returnedFromService]);
        return service.retrieve({ sort: {}, page: 0, size: 10 }).then(res => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should not return a list of PacienteMedicamento', async () => {
        axiosStub.get.rejects(error);

        return service
          .retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a PacienteMedicamento', async () => {
        axiosStub.delete.resolves({ ok: true });
        return service.delete(123).then(res => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a PacienteMedicamento', async () => {
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
