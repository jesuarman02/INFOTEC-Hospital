/* tslint:disable max-line-length */
import axios from 'axios';
import sinon from 'sinon';
import dayjs from 'dayjs';

import InfoSocioeconomicaService from './info-socioeconomica.service';
import { DATE_FORMAT } from '@/shared/composables/date-format';
import { InfoSocioeconomica } from '@/shared/model/pacientems/info-socioeconomica.model';

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
  describe('InfoSocioeconomica Service', () => {
    let service: InfoSocioeconomicaService;
    let elemDefault;
    let currentDate: Date;

    beforeEach(() => {
      service = new InfoSocioeconomicaService();
      currentDate = new Date();
      elemDefault = new InfoSocioeconomica(
        123,
        'AAAAAAA',
        'AAAAAAA',
        0,
        0,
        'AAAAAAA',
        0,
        0,
        0,
        0,
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        0,
        false,
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        0,
        currentDate,
      );
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = { fechaActualizacion: dayjs(currentDate).format(DATE_FORMAT), ...elemDefault };
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

      it('should create a InfoSocioeconomica', async () => {
        const returnedFromService = { id: 123, fechaActualizacion: dayjs(currentDate).format(DATE_FORMAT), ...elemDefault };
        const expected = { fechaActualizacion: currentDate, ...returnedFromService };

        axiosStub.post.resolves({ data: returnedFromService });
        return service.create({}).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not create a InfoSocioeconomica', async () => {
        axiosStub.post.rejects(error);

        return service
          .create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a InfoSocioeconomica', async () => {
        const returnedFromService = {
          tipoVivienda: 'BBBBBB',
          materialVivienda: 'BBBBBB',
          numeroHabitaciones: 1,
          numeroHabitantes: 1,
          serviciosDisponibles: 'BBBBBB',
          ingresoMensual: 1,
          ingresoMensualHogar: 1,
          gastoMensual: 1,
          personasDependientes: 1,
          apoyosGubernamentales: 'BBBBBB',
          ocupacionActual: 'BBBBBB',
          condicionLaboral: 'BBBBBB',
          tipoEmpleo: 'BBBBBB',
          lugarTrabajo: 'BBBBBB',
          tiempoEmpleado: 'BBBBBB',
          gradoMaximoEstudios: 'BBBBBB',
          aniosEstudio: 1,
          estudia: true,
          institucionMedica: 'BBBBBB',
          tipoAfiliacion: 'BBBBBB',
          numeroAfiliacion: 'BBBBBB',
          medioTransporte: 'BBBBBB',
          tiempoTraslado: 1,
          fechaActualizacion: dayjs(currentDate).format(DATE_FORMAT),
          ...elemDefault,
        };

        const expected = { fechaActualizacion: currentDate, ...returnedFromService };
        axiosStub.put.resolves({ data: returnedFromService });

        return service.update(expected).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not update a InfoSocioeconomica', async () => {
        axiosStub.put.rejects(error);

        return service
          .update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should partial update a InfoSocioeconomica', async () => {
        const patchObject = {
          materialVivienda: 'BBBBBB',
          numeroHabitaciones: 1,
          serviciosDisponibles: 'BBBBBB',
          personasDependientes: 1,
          ocupacionActual: 'BBBBBB',
          condicionLaboral: 'BBBBBB',
          tipoEmpleo: 'BBBBBB',
          lugarTrabajo: 'BBBBBB',
          tiempoEmpleado: 'BBBBBB',
          tipoAfiliacion: 'BBBBBB',
          medioTransporte: 'BBBBBB',
          ...new InfoSocioeconomica(),
        };
        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = { fechaActualizacion: currentDate, ...returnedFromService };
        axiosStub.patch.resolves({ data: returnedFromService });

        return service.partialUpdate(patchObject).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not partial update a InfoSocioeconomica', async () => {
        axiosStub.patch.rejects(error);

        return service
          .partialUpdate({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of InfoSocioeconomica', async () => {
        const returnedFromService = {
          tipoVivienda: 'BBBBBB',
          materialVivienda: 'BBBBBB',
          numeroHabitaciones: 1,
          numeroHabitantes: 1,
          serviciosDisponibles: 'BBBBBB',
          ingresoMensual: 1,
          ingresoMensualHogar: 1,
          gastoMensual: 1,
          personasDependientes: 1,
          apoyosGubernamentales: 'BBBBBB',
          ocupacionActual: 'BBBBBB',
          condicionLaboral: 'BBBBBB',
          tipoEmpleo: 'BBBBBB',
          lugarTrabajo: 'BBBBBB',
          tiempoEmpleado: 'BBBBBB',
          gradoMaximoEstudios: 'BBBBBB',
          aniosEstudio: 1,
          estudia: true,
          institucionMedica: 'BBBBBB',
          tipoAfiliacion: 'BBBBBB',
          numeroAfiliacion: 'BBBBBB',
          medioTransporte: 'BBBBBB',
          tiempoTraslado: 1,
          fechaActualizacion: dayjs(currentDate).format(DATE_FORMAT),
          ...elemDefault,
        };
        const expected = { fechaActualizacion: currentDate, ...returnedFromService };
        axiosStub.get.resolves([returnedFromService]);
        return service.retrieve().then(res => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should not return a list of InfoSocioeconomica', async () => {
        axiosStub.get.rejects(error);

        return service
          .retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a InfoSocioeconomica', async () => {
        axiosStub.delete.resolves({ ok: true });
        return service.delete(123).then(res => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a InfoSocioeconomica', async () => {
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
