package ${basePackage}.service.impl.${sign};

import ${basePackage}.dao.mapper.${sign}.${modelNameUpperCamel}Mapper;
import ${basePackage}.model.${sign}.${modelNameUpperCamel};
import ${basePackage}.service.${sign}.${modelNameUpperCamel}Service;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


/**
 *
 * Created by ${author} on ${date}.
 */
@Service
public class ${modelNameUpperCamel}ServiceImpl implements ${modelNameUpperCamel}Service {
    @Resource
    private ${modelNameUpperCamel}Mapper ${modelNameLowerCamel}Mapper;

}
