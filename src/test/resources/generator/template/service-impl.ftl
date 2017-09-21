package ${basePackage}.service.impl.${sign};

import ${basePackage}.dao.mapper.${sign}.${modelNameUpperCamel}Mapper;
import ${basePackage}.model.${sign}.${modelNameUpperCamel};
import ${basePackage}.service.${sign}.${modelNameUpperCamel}Service;
import ${basePackage}.service.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * Created by ${author} on ${date}.
 */
@Service
public class ${modelNameUpperCamel}ServiceImpl extends AbstractService<${modelNameUpperCamel}> implements ${modelNameUpperCamel}Service {

    @Autowired
    private ${modelNameUpperCamel}Mapper ${modelNameLowerCamel}Mapper;

}
