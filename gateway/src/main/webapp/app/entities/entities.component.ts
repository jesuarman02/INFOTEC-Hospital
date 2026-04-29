import { defineComponent, provide } from 'vue';

import CitaService from './pacientems/cita/cita.service';
import UserService from '@/entities/user/user.service';
// jhipster-needle-add-entity-service-to-entities-component-import - JHipster will import entities services here

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'Entities',
  setup() {
    provide('userService', () => new UserService());
    provide('citaService', () => new CitaService());
    // jhipster-needle-add-entity-service-to-entities-component - JHipster will import entities services here
  },
});
