/* tslint:disable max-line-length */
import axios from 'axios';
import sinon from 'sinon';
import dayjs from 'dayjs';

import CitaService from './cita.service';
import { DATE_TIME_FORMAT } from '@/shared/composables/date-format';
import { Cita } from '@/shared/model/pacientems/cita.model';

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
  describe('Cita Service', () => {
    let service: CitaService;
    let elemDefault;
    let currentDate: Date;

    beforeEach(() => {
      service = new CitaService();
      currentDate = new Date();
      elemDefault = new Cita(123, 0, 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', currentDate, 'AAAAAAA');
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = { fechaHora: dayjs(currentDate).format(DATE_TIME_FORMAT), ...elemDefault };
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

      it('should create a Cita', async () => {
        const returnedFromService = { id: 123, fechaHora: dayjs(currentDate).format(DATE_TIME_FORMAT), ...elemDefault };
        const expected = { fechaHora: currentDate, ...returnedFromService };

        axiosStub.post.resolves({ data: returnedFromService });
        return service.create({}).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not create a Cita', async () => {
        axiosStub.post.rejects(error);

        return service
          .create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a Cita', async () => {
        const returnedFromService = {
          ecu: 1,
          nombre: 'BBBBBB',
          apellidoPaterno: 'BBBBBB',
          apellidoMaterno: 'BBBBBB',
          fechaHora: dayjs(currentDate).format(DATE_TIME_FORMAT),
          motivo: 'BBBBBB',
          ...elemDefault,
        };

        const expected = { fechaHora: currentDate, ...returnedFromService };
        axiosStub.put.resolves({ data: returnedFromService });

        return service.update(expected).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not update a Cita', async () => {
        axiosStub.put.rejects(error);

        return service
          .update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should partial update a Cita', async () => {
        const patchObject = {
          ecu: 1,
          apellidoPaterno: 'BBBBBB',
          fechaHora: dayjs(currentDate).format(DATE_TIME_FORMAT),
          motivo: 'BBBBBB',
          ...new Cita(),
        };
        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = { fechaHora: currentDate, ...returnedFromService };
        axiosStub.patch.resolves({ data: returnedFromService });

        return service.partialUpdate(patchObject).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not partial update a Cita', async () => {
        axiosStub.patch.rejects(error);

        return service
          .partialUpdate({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of Cita', async () => {
        const returnedFromService = {
          ecu: 1,
          nombre: 'BBBBBB',
          apellidoPaterno: 'BBBBBB',
          apellidoMaterno: 'BBBBBB',
          fechaHora: dayjs(currentDate).format(DATE_TIME_FORMAT),
          motivo: 'BBBBBB',
          ...elemDefault,
        };
        const expected = { fechaHora: currentDate, ...returnedFromService };
        axiosStub.get.resolves([returnedFromService]);
        return service.retrieve({ sort: {}, page: 0, size: 10 }).then(res => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should not return a list of Cita', async () => {
        axiosStub.get.rejects(error);

        return service
          .retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a Cita', async () => {
        axiosStub.delete.resolves({ ok: true });
        return service.delete(123).then(res => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a Cita', async () => {
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
