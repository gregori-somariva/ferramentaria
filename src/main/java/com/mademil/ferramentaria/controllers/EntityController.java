package com.mademil.ferramentaria.controllers;

import com.mademil.ferramentaria.entities.Chuck;
import com.mademil.ferramentaria.entities.GlChuck;
import com.mademil.ferramentaria.entities.Tool;
import com.mademil.ferramentaria.entities.Template;
import com.mademil.ferramentaria.entities.Vise;
import com.mademil.ferramentaria.entities.YokeRing;
import com.mademil.ferramentaria.entities.User;
import com.mademil.ferramentaria.repositories.UserRepository;
import com.mademil.ferramentaria.service.ChuckService;
import com.mademil.ferramentaria.service.GlChuckService;
import com.mademil.ferramentaria.service.ToolService;
import com.mademil.ferramentaria.service.TemplateService;
import com.mademil.ferramentaria.service.ViseService;
import com.mademil.ferramentaria.service.YokeRingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.lang.reflect.Method;

@Controller
public class EntityController {
    @Autowired
    private ChuckService chuckService;

    @Autowired
    private ToolService toolService;

    @Autowired
    private TemplateService templateService;

    @Autowired
    private ViseService viseService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    GlChuckService glChuckService;

    @Autowired
    YokeRingService yokeRingService;

    // Disallow images to prevent @ModelAttribute from
    // auto binding the image file to the image field on each entity,
    // since the they expect a byte[] of the image file, the conversion 
    // must be made before attributing the file to the field
    @InitBinder
    public void initBinder(WebDataBinder binder) { 
        binder.setDisallowedFields("chuckImage", "toolImage", "templateImage", "viseImage", "glChuckImage", "yokeRingImage"); 
    }

    @GetMapping("/entidades")
    public String serveEntitiesPage(
        @RequestParam(value = "error", required = false) String error,
        @AuthenticationPrincipal UserDetails springUser,
        Model model){

        User user = userRepository.findByUsername(springUser.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (error != null) {
            model.addAttribute("error", error);
        }

        List<Chuck> chucks = chuckService.getAllActiveChucks();
        List<Tool> tools = toolService.getAllActiveTools();
        List<Template> templates = templateService.getAllActiveTemplates();
        List<Vise> vises = viseService.getAllActiveVises();
        List<GlChuck> glChucks = glChuckService.getAllActiveGlChucks();
        List<YokeRing> yokeRings = yokeRingService.getAllActiveYokeRings();

        model.addAttribute("user", user);
        model.addAttribute("chucks", chucks);
        model.addAttribute("tools", tools);
        model.addAttribute("templates", templates);
        model.addAttribute("vises", vises);
        model.addAttribute("glChucks", glChucks);
        model.addAttribute("yokeRings", yokeRings);

        return "entities";
    }

    
    /* Every single form controller that is used to persist
     * an entity to the database will optionally receive
     * an ID as a parameter. If the ID provided matches any
     * record on the relevant table, that record is passed
     * to the view so that the template engine can auto-fill
     * the form with the data from that record.
     * 
     * This was done in order to allow users to update a property
     * of an entity without having to copy paste the properties that 
     * they are not interested in changing.
     */

    private <T> String handleForm(
        Integer entityId,
        String errorMessage,
        Model model,
        Supplier<Optional<T>> entitySupplier,
        String formViewName,
        String entityName) {

        if (errorMessage != null) {
            model.addAttribute("error", errorMessage);
        }

        if (entityId != null) {
            Optional<T> optionalEntity = entitySupplier.get();
            if (optionalEntity.isPresent()) {
                model.addAttribute(entityName, optionalEntity.get());
            } else {
                model.addAttribute("error", entityName + " não encontrado: " + entityId);
            }
        }

        return formViewName;
    }

    @GetMapping("/formulario-castanha")
    public String addChuckForm(
        @RequestParam(value = "chuckId", required = false) Integer chuckId,
        @RequestParam(value = "error", required = false) String errorMessage,
        Model model) {

        return handleForm(chuckId, errorMessage, model, 
                          () -> chuckService.getChuckById(chuckId), 
                          "form-chuck", "chuck");
    }

    @GetMapping("/formulario-ferramenta")
    public String addToolForm(
        @RequestParam(value = "toolId", required = false) Integer toolId,
        @RequestParam(value = "error", required = false) String errorMessage,
        Model model) {

        return handleForm(toolId, errorMessage, model, 
                          () -> toolService.getToolById(toolId), 
                          "form-tool", "tool");
    }

    @GetMapping("/formulario-gabarito")
    public String addTemplateForm(
        @RequestParam(value = "templateId", required = false) Integer templateId,
        @RequestParam(value = "error", required = false) String errorMessage,
        Model model) {

        return handleForm(templateId, errorMessage, model, 
                          () -> templateService.getTemplateById(templateId), 
                          "form-template", "template");
    }

    @GetMapping("/formulario-morsa")
    public String addViseForm(
        @RequestParam(value = "viseId", required = false) Integer viseId,
        @RequestParam(value = "error", required = false) String errorMessage,
        Model model) {

        return handleForm(viseId, errorMessage, model, 
                          () -> viseService.getViseById(viseId), 
                          "form-vise", "vise");
    }

    @GetMapping("/formulario-castanhagl")
    public String addGlChuckForm(
        @RequestParam(value = "glChuckId", required = false) Integer glChuckId,
        @RequestParam(value = "error", required = false) String errorMessage,
        Model model) {

        return handleForm(glChuckId, errorMessage, model, 
                          () -> glChuckService.getGlChuckById(glChuckId), 
                          "form-gl-chuck", "glChuck");
    }

    @GetMapping("/formulario-anelyoke")
    public String addYokeRingForm(
        @RequestParam(value = "yokeRingId", required = false) Integer yokeRingId,
        @RequestParam(value = "error", required = false) String errorMessage,
        Model model) {

        return handleForm(yokeRingId, errorMessage, model, 
                          () -> yokeRingService.getYokeRingById(yokeRingId), 
                          "form-yoke-ring", "yokeRing");
    }

    private <T> String handleEntityWithImage(
        T entity,
        Integer entityId,
        MultipartFile image,
        String entityName,
        Consumer<T> service,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes,
        String formRedirectUrl
    ) {
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getAllErrors().get(0).getDefaultMessage();
            redirectAttributes.addFlashAttribute("error", errorMessage);
            return "redirect:" + formRedirectUrl;
        }

        try {
            if (entityId != null) {
                Method setIdMethod = entity.getClass().getMethod("set" + entityName + "Id", Integer.class);
                setIdMethod.invoke(entity, entityId);
            }
            
            Method setNameMethod = entity.getClass().getMethod("set" + entityName + "Name", String.class);
            Method setLocationMethod = entity.getClass().getMethod("set" + entityName + "Location", String.class);
            Method setImageMethod = entity.getClass().getMethod("set" + entityName + "Image", byte[].class);
            Method getNameMethod = entity.getClass().getMethod("get" + entityName + "Name");
            Method getLocationMethod = entity.getClass().getMethod("get" + entityName + "Location");

            String name = (String) getNameMethod.invoke(entity);
            String location = (String) getLocationMethod.invoke(entity);

            setNameMethod.invoke(entity, name.toUpperCase());
            setLocationMethod.invoke(entity, location.toUpperCase());
            setImageMethod.invoke(entity, image.getBytes());

            Method setIsDeletedMethod = entity.getClass().getMethod("setIsDeleted", Boolean.class);
            setIsDeletedMethod.invoke(entity, false);

            // Save entity
            service.accept(entity);

            return "redirect:/entidades";

        } catch (DataIntegrityViolationException e) {
            System.err.println("Data Integrity error: " + e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Integridade de dados violada");
            return "redirect:" + formRedirectUrl;
        } catch (IOException e) {
            System.err.println("IO Error: " + e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Não foi possível ler a imagem");
            return "redirect:" + formRedirectUrl;
        } catch (Exception e) {
            System.err.println("Error invoking methods: " + e.getMessage());
            return "redirect:" + formRedirectUrl;
        }
    }
    
    @PostMapping("/adicionar-castanha")
    public String addChuckToDatabase(
        @RequestParam(value = "chuckId", required = false) Integer chuckId,
        @RequestParam("chuckImage") MultipartFile chuckImage,
        @Valid @ModelAttribute Chuck chuck,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes
    ) {
        return handleEntityWithImage(
            chuck,
            chuckId,
            chuckImage,
            "Chuck",
            chuckService::saveChuck,
            bindingResult,
            redirectAttributes,
            "/formulario-castanha"
        );
    }
    
    @PostMapping("/adicionar-gabarito")
    public String addTemplateToDatabase(
        @RequestParam(value = "templateId", required = false) Integer templateId,
        @RequestParam("templateImage") MultipartFile templateImage,
        @Valid @ModelAttribute Template template,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes
    ) {
        return handleEntityWithImage(
            template,
            templateId,
            templateImage,
            "Template",
            templateService::saveTemplate,
            bindingResult,
            redirectAttributes,
            "/formulario-gabarito"
        );
    }
    
    @PostMapping("/adicionar-morsa")
    public String addViseToDatabase(
        @RequestParam(value = "viseId", required = false) Integer viseId,
        @RequestParam("viseImage") MultipartFile viseImage,
        @Valid @ModelAttribute Vise vise,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes
    ) {
        return handleEntityWithImage(
            vise,
            viseId,
            viseImage,
            "Vise",
            viseService::saveVise,
            bindingResult,
            redirectAttributes,
            "/formulario-morsa"
        );
    }
    
    @PostMapping("/adicionar-castanhagl")
    public String addGlChuckToDatabase(
        @RequestParam(value = "glChuckId", required = false) Integer glChuckId,
        @RequestParam("glChuckImage") MultipartFile glChuckImage,
        @Valid @ModelAttribute GlChuck glChuck,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes
    ) {
        return handleEntityWithImage(
            glChuck,
            glChuckId,
            glChuckImage,
            "GlChuck",
            glChuckService::saveGlChuck,
            bindingResult,
            redirectAttributes,
            "/formulario-castanhagl"
        );
    }

    @PostMapping("/adicionar-anelyoke")
    public String addYokeRingToDatabase(
        @RequestParam(value = "yokeRingId", required = false) Integer yokeRingId,
        @RequestParam("yokeRingImage") MultipartFile yokeRingImage,
        @Valid @ModelAttribute YokeRing yokeRing,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes
    ) {
        return handleEntityWithImage(
            yokeRing,
            yokeRingId,
            yokeRingImage,
            "YokeRing",
            yokeRingService::saveYokeRing,
            bindingResult,
            redirectAttributes,
            "/formulario-anelyoke"
        );
    }

    @PostMapping("/adicionar-ferramenta")
    public String addToolToDatabase(
        @RequestParam(value = "toolId", required = false) Integer toolId,
        @RequestParam("toolImage") MultipartFile toolImage,
        @Valid @ModelAttribute Tool tool,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getAllErrors().get(0).getDefaultMessage();
            redirectAttributes.addFlashAttribute("error", errorMessage);
            return "redirect:/formulario-ferramenta";
        }

        try {
            if (toolId != null) {
                tool.setToolId(toolId);
            }
            tool.setToolName(tool.getToolName().toUpperCase());
            tool.setToolImage(toolImage.getBytes());
            tool.setIsDeleted(false);
            toolService.saveTool(tool);

            return "redirect:/entidades";
        }catch (DataIntegrityViolationException e){
            System.err.println("Data Integrity error " + e.getMostSpecificCause());
            redirectAttributes.addAttribute("error", "Integridade de dados violada");
            return "redirect:/formulario-ferramenta";
        }catch (IOException e){
            System.err.println("IO Error: " + e.getMessage());
            redirectAttributes.addAttribute("error", "Não foi possível ler a imagem");
            return "redirect:/formulario-ferramenta";
        }
    }

    private <T> boolean deactivateEntityById(
        Function<Integer, Optional<T>> findById,
        Consumer<T> saveEntity,
        Integer entityId) {

        Optional<T> optionalEntity = findById.apply(entityId);

        if (optionalEntity.isPresent()) {
            T entity = optionalEntity.get();
            try {
                Method setIsDeleted = entity.getClass().getMethod("setIsDeleted", Boolean.class);
                setIsDeleted.invoke(entity, true);
                saveEntity.accept(entity);
                return true;
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException("Erro ao desativar ", e);
            }
        }
        return false;
    }

   @PostMapping("/inativar-entidade")
    public String deactivateEntity(
        @AuthenticationPrincipal UserDetails springUser,
        @RequestParam("entityIdentifier") String entityIdentifier,
        @RequestParam(value = "entityId", required = false) Integer entityId,
        RedirectAttributes redirectAttributes) {

        User user = userRepository.findByUsername(springUser.getUsername())
            .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getRole().equalsIgnoreCase("ADMIN")) {
            redirectAttributes.addAttribute("error", "Usuário sem permissão para desativar entidades");
            return "redirect:/entidades";
        }

        if (entityId == null) {
            redirectAttributes.addAttribute("error", "Erro ao inativar: nenhum ID foi providenciado");
            return "redirect:/entidades";
        }

        boolean success;
        String errorMessage;

        switch (entityIdentifier.toUpperCase()) {
            case "CHUCK":
                success = deactivateEntityById(chuckService::getChuckById, chuckService::saveChuck, entityId);
                errorMessage = "Erro ao inativar castanha: castanha com o ID " + entityId + " não existe";
                break;

            case "TOOL":
                success = deactivateEntityById(toolService::getToolById, toolService::saveTool, entityId);
                errorMessage = "Erro ao inativar ferramenta: ferramenta com o ID " + entityId + " não existe";
                break;

            case "TEMPLATE":
                success = deactivateEntityById(templateService::getTemplateById, templateService::saveTemplate, entityId);
                errorMessage = "Erro ao inativar gabarito: gabarito com o ID " + entityId + " não existe";
                break;

            case "VISE":
                success = deactivateEntityById(viseService::getViseById, viseService::saveVise, entityId);
                errorMessage = "Erro ao inativar morsa: morsa com o ID " + entityId + " não existe";
                break;

            case "GLCHUCK":
                success = deactivateEntityById(glChuckService::getGlChuckById, glChuckService::saveGlChuck, entityId);
                errorMessage = "Erro ao inativar castanha GL: castanha com o ID " + entityId + " não existe";
                break;

            case "YOKERING":
                success = deactivateEntityById(yokeRingService::getYokeRingById, yokeRingService::saveYokeRing, entityId);
                errorMessage = "Erro ao inativar anel yoke: anel com o ID " + entityId + " não existe";
                break;

            default:
                redirectAttributes.addAttribute("error", "Erro inesperado");
                return "redirect:/entidades";
        }

        if (!success) {
            redirectAttributes.addAttribute("error", errorMessage);
        }

        return "redirect:/entidades";
    }
}
