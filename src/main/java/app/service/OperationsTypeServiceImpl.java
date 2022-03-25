package app.service;

import app.document.OperationsType;
import app.repository.OperationsTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OperationsTypeServiceImpl implements  OperationsTypeService {
    private final OperationsTypeRepository operationsTypeRepository;
    @Override
    public boolean isOperationsTypeDefined() {
        List<OperationsType> collections = operationsTypeRepository.findAll().collectList().block();
        return !CollectionUtils.isEmpty(collections);
    }


    @Override
    public void createOperationsType(OperationsType operationsType) {
        operationsTypeRepository.save(operationsType);
    }

   // @PostConstruct
    public void initilize(){
        if (isOperationsTypeDefined()) {
            System.out.println("All operations Types r created");
        } else {
            OperationsType one = OperationsType.builder().operationsTypeId("1").description("Normal Purchase").multiplier(-1).build();
            OperationsType two = OperationsType.builder().operationsTypeId("2").description("Purchase with installments").multiplier(1).build();
            OperationsType three = OperationsType.builder().operationsTypeId("3").description("Withdrawal").multiplier(-1).build();
            OperationsType four = OperationsType.builder().operationsTypeId("3").description("Credit Voucher").multiplier(1).build();
            List<OperationsType> operationsTypeList = List.of(one,two,three,four);
            operationsTypeList.forEach(this::createOperationsType);

        }
    }
}
